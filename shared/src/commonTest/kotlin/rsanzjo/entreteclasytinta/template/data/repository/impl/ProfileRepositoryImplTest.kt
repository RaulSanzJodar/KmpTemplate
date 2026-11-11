package rsanzjo.entreteclasytinta.template.data.repository.impl

import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.test.runTest
import rsanzjo.entreteclasytinta.template.domain.entity.UserProfile
import rsanzjo.entreteclasytinta.template.domain.error.DomainError
import rsanzjo.entreteclasytinta.template.testing.SESSION_JSON
import rsanzjo.entreteclasytinta.template.testing.testSupabaseClient
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class ProfileRepositoryImplTest {

    private suspend fun signedInRepository(profilesResponse: Pair<HttpStatusCode, String>): ProfileRepositoryImpl {
        val supabase = testSupabaseClient { path ->
            if (path.startsWith("rest/")) profilesResponse else HttpStatusCode.OK to SESSION_JSON
        }
        AuthRepositoryImpl(supabase).signIn("ada@example.com", "secret")
        return ProfileRepositoryImpl(supabase)
    }

    @Test
    fun getCurrentProfile_mapsTheProfilesRow() = runTest {
        val row = """{"id":"user-1","name":"Ada","email":"ada@example.com","created_at":"2026-01-01"}"""
        val repository = signedInRepository(HttpStatusCode.OK to "[$row]")
        assertEquals(
            UserProfile(id = "user-1", name = "Ada", email = "ada@example.com"),
            repository.getCurrentProfile()
        )
    }

    @Test
    fun getCurrentProfile_withoutRow_fallsBackToTheAccountEmail() = runTest {
        val repository = signedInRepository(HttpStatusCode.OK to "[]")
        assertEquals(UserProfile(id = "user-1", email = "ada@example.com"), repository.getCurrentProfile())
    }

    @Test
    fun getCurrentProfile_onServerError_throwsHttp() = runTest {
        val repository = signedInRepository(HttpStatusCode.Forbidden to """{"message":"permission denied"}""")
        assertEquals(403, assertFailsWith<DomainError.Http> { repository.getCurrentProfile() }.code)
    }

    @Test
    fun getCurrentProfile_withoutSession_throwsUnauthorized() = runTest {
        val repository = ProfileRepositoryImpl(testSupabaseClient { HttpStatusCode.OK to "[]" })
        assertFailsWith<DomainError.Unauthorized> { repository.getCurrentProfile() }
    }
}
