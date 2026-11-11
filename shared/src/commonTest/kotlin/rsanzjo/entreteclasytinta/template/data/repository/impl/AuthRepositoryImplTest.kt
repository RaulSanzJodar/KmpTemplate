package rsanzjo.entreteclasytinta.template.data.repository.impl

import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import rsanzjo.entreteclasytinta.template.domain.entity.SessionState
import rsanzjo.entreteclasytinta.template.domain.error.DomainError
import rsanzjo.entreteclasytinta.template.testing.FakeSecureStorage
import rsanzjo.entreteclasytinta.template.testing.SESSION_JSON
import rsanzjo.entreteclasytinta.template.testing.authErrorJson
import rsanzjo.entreteclasytinta.template.testing.testSupabaseClient
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class AuthRepositoryImplTest {

    private val sessionKey = "supabase_session"

    @Test
    fun sessionState_withoutStoredSession_isUnauthenticated() = runTest {
        val repository = AuthRepositoryImpl(testSupabaseClient { HttpStatusCode.OK to "{}" })
        assertEquals(SessionState.Unauthenticated(), repository.sessionState.first { it != SessionState.Loading })
    }

    @Test
    fun signIn_storesSessionEncryptedAndBecomesAuthenticated() = runTest {
        val storage = FakeSecureStorage()
        val repository = AuthRepositoryImpl(testSupabaseClient(storage) { HttpStatusCode.OK to SESSION_JSON })

        repository.signIn("ada@example.com", "secret")

        assertEquals(SessionState.Authenticated, repository.sessionState.first { it != SessionState.Loading })
        assertNotNull(storage.getString(sessionKey))
    }

    @Test
    fun signIn_withWrongPassword_throwsInvalidCredentials() = runTest {
        val repository = AuthRepositoryImpl(
            testSupabaseClient { HttpStatusCode.BadRequest to authErrorJson("invalid_credentials") }
        )
        assertFailsWith<DomainError.InvalidCredentials> { repository.signIn("ada@example.com", "wrong") }
    }

    @Test
    fun signUp_withSessionInResponse_returnsTrue() = runTest {
        val repository = AuthRepositoryImpl(testSupabaseClient { HttpStatusCode.OK to SESSION_JSON })
        assertTrue(repository.signUp("ada@example.com", "secret"))
    }

    @Test
    fun signUp_whenEmailMustBeConfirmed_returnsFalse() = runTest {
        val identity = """{"id":"i","identity_data":{},"user_id":"user-1","provider":"email"}"""
        val pendingUser =
            """{"id":"user-1","aud":"authenticated","email":"ada@example.com","identities":[$identity]}"""
        val repository = AuthRepositoryImpl(testSupabaseClient { HttpStatusCode.OK to pendingUser })
        assertFalse(repository.signUp("ada@example.com", "secret"))
    }

    @Test
    fun signUp_withAlreadyRegisteredEmail_throwsUserAlreadyExists() = runTest {
        // Con confirmación por correo, Supabase responde 200 con un usuario sin identidades.
        val obfuscatedUser = """{"id":"user-1","aud":"authenticated","email":"ada@example.com","identities":[]}"""
        val repository = AuthRepositoryImpl(testSupabaseClient { HttpStatusCode.OK to obfuscatedUser })
        assertFailsWith<DomainError.UserAlreadyExists> { repository.signUp("ada@example.com", "secret") }
    }

    @Test
    fun signOut_clearsSessionEvenWhenLogoutRequestFails() = runTest {
        val storage = FakeSecureStorage()
        val repository = AuthRepositoryImpl(
            testSupabaseClient(storage) { path ->
                if (path.endsWith("logout")) {
                    HttpStatusCode.InternalServerError to "{}"
                } else {
                    HttpStatusCode.OK to SESSION_JSON
                }
            }
        )
        repository.signIn("ada@example.com", "secret")

        repository.signOut()

        // Cierre voluntario: no debe mostrarse como sesión caducada.
        assertEquals(
            SessionState.Unauthenticated(expired = false),
            repository.sessionState.first { it is SessionState.Unauthenticated }
        )
        assertNull(storage.getString(sessionKey))
    }

    @Test
    fun sendPasswordReset_whenRateLimited_throwsTooManyRequests() = runTest {
        val repository = AuthRepositoryImpl(
            testSupabaseClient { HttpStatusCode.TooManyRequests to authErrorJson("over_email_send_rate_limit") }
        )
        assertFailsWith<DomainError.TooManyRequests> { repository.sendPasswordReset("ada@example.com") }
    }
}
