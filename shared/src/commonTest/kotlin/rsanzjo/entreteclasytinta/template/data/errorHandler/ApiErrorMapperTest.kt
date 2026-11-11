package rsanzjo.entreteclasytinta.template.data.errorHandler

import io.github.jan.supabase.auth.exception.AuthRestException
import io.github.jan.supabase.exceptions.UnknownRestException
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.test.runTest
import kotlinx.io.IOException
import rsanzjo.entreteclasytinta.template.domain.error.DomainError
import kotlin.coroutines.cancellation.CancellationException
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertIs
import kotlin.test.assertSame

class ApiErrorMapperTest {

    private suspend fun responseWith(status: HttpStatusCode): HttpResponse =
        HttpClient(MockEngine { respond("", status) }).get("http://localhost/")

    private suspend fun authError(code: String, status: HttpStatusCode = HttpStatusCode.BadRequest) =
        AuthRestException(code, "description", responseWith(status)).toDomainError()

    @Test
    fun ioException_mapsToNetwork() {
        assertIs<DomainError.Network>(IOException("boom").toDomainError())
    }

    @Test
    fun http401_mapsToUnauthorized() = runTest {
        val error = ResponseException(responseWith(HttpStatusCode.Unauthorized), "")
        assertIs<DomainError.Unauthorized>(error.toDomainError())
    }

    @Test
    fun http429_mapsToTooManyRequests() = runTest {
        val error = ResponseException(responseWith(HttpStatusCode.TooManyRequests), "")
        assertIs<DomainError.TooManyRequests>(error.toDomainError())
    }

    @Test
    fun supabaseRestError_mapsToHttpWithCode() = runTest {
        val error = UnknownRestException("boom", responseWith(HttpStatusCode.InternalServerError)).toDomainError()
        assertEquals(500, assertIs<DomainError.Http>(error).code)
    }

    @Test
    fun authErrorCodes_mapToTheirDomainError() = runTest {
        assertIs<DomainError.InvalidCredentials>(authError("invalid_credentials"))
        assertIs<DomainError.EmailNotConfirmed>(authError("email_not_confirmed"))
        assertIs<DomainError.UserAlreadyExists>(authError("user_already_exists"))
        assertIs<DomainError.WeakPassword>(authError("weak_password"))
        assertIs<DomainError.TooManyRequests>(authError("over_request_rate_limit"))
        assertIs<DomainError.Unauthorized>(authError("session_expired"))
    }

    @Test
    fun unknownAuthErrorCode_fallsBackToTheHttpStatus() = runTest {
        val error = authError("something_new", HttpStatusCode.UnprocessableEntity)
        assertEquals(422, assertIs<DomainError.Http>(error).code)
    }

    @Test
    fun domainError_isReturnedAsIs() {
        val error = DomainError.Network()
        assertSame(error, error.toDomainError())
    }

    @Test
    fun cancellation_isReturnedAsIs() {
        val error = CancellationException("cancelled")
        assertSame(error, error.toDomainError())
    }

    @Test
    fun domainCall_rethrowsMappedErrors() = runTest {
        assertFailsWith<DomainError.Network> { domainCall { throw IOException("boom") } }
    }
}
