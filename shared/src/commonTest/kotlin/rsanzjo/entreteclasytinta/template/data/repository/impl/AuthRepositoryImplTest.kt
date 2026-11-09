package rsanzjo.entreteclasytinta.template.data.repository.impl

import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import rsanzjo.entreteclasytinta.template.data.datasource.SessionManager
import rsanzjo.entreteclasytinta.template.data.datasource.api.AppApi
import rsanzjo.entreteclasytinta.template.data.datasource.api.client.createHttpClient
import rsanzjo.entreteclasytinta.template.domain.error.DomainError
import rsanzjo.entreteclasytinta.template.testing.FakeSecureStorage
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class AuthRepositoryImplTest {

    private val json = headersOf(HttpHeaders.ContentType, "application/json")

    private class Fixture(val sessionManager: SessionManager, val repository: AuthRepositoryImpl, val api: AppApi)

    private fun fixture(handler: (path: String) -> Pair<HttpStatusCode, String>): Fixture {
        val sessionManager = SessionManager(FakeSecureStorage())
        val engine = MockEngine { request ->
            val (status, body) = handler(request.url.encodedPath.trimStart('/'))
            respond(body, status, json)
        }
        val api = AppApi(createHttpClient(sessionManager, engine))
        return Fixture(sessionManager, AuthRepositoryImpl(api, sessionManager), api)
    }

    @Test
    fun signIn_savesTokensFromResponse() = runTest {
        val f = fixture { HttpStatusCode.OK to """{"token":"t","refreshToken":"r"}""" }
        f.repository.signIn("a@b.c", "secret")
        assertEquals("t", f.sessionManager.token.first())
        assertEquals("r", f.sessionManager.refreshToken.first())
    }

    @Test
    fun isSignedIn_reflectsStoredToken() = runTest {
        val f = fixture { HttpStatusCode.OK to "{}" }
        assertFalse(f.repository.isSignedIn())
        f.sessionManager.saveTokens("t", "r")
        assertTrue(f.repository.isSignedIn())
    }

    @Test
    fun signOut_clearsSessionEvenWhenLogoutRequestFails() = runTest {
        val f = fixture { HttpStatusCode.InternalServerError to "{}" }
        f.sessionManager.saveTokens("t", "r")
        f.repository.signOut()
        assertNull(f.sessionManager.token.first())
        assertNull(f.sessionManager.refreshToken.first())
    }

    @Test
    fun unauthorizedWithoutRefreshToken_clearsSessionAndThrowsUnauthorized() = runTest {
        val f = fixture { HttpStatusCode.Unauthorized to "{}" }
        f.sessionManager.saveToken("old")
        assertFailsWith<DomainError.Unauthorized> { f.api.getProfile() }
        assertNull(f.sessionManager.token.first())
    }
}
