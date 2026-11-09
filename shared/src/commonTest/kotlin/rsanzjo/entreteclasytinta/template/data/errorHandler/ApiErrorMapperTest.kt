package rsanzjo.entreteclasytinta.template.data.errorHandler

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.test.runTest
import kotlinx.io.IOException
import rsanzjo.entreteclasytinta.template.domain.error.DomainError
import kotlin.coroutines.cancellation.CancellationException
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertSame

class ApiErrorMapperTest {

    private suspend fun exceptionFor(status: HttpStatusCode): ResponseException {
        val client = HttpClient(MockEngine { respond("", status) })
        return ResponseException(client.get("http://localhost/"), "")
    }

    @Test
    fun ioException_mapsToNetwork() {
        assertIs<DomainError.Network>(IOException("boom").toDomainError())
    }

    @Test
    fun http401_mapsToUnauthorized() = runTest {
        assertIs<DomainError.Unauthorized>(exceptionFor(HttpStatusCode.Unauthorized).toDomainError())
    }

    @Test
    fun http429_mapsToTooManyRequests() = runTest {
        assertIs<DomainError.TooManyRequests>(exceptionFor(HttpStatusCode.TooManyRequests).toDomainError())
    }

    @Test
    fun http500_mapsToHttpWithCode() = runTest {
        val error = exceptionFor(HttpStatusCode.InternalServerError).toDomainError()
        assertEquals(500, assertIs<DomainError.Http>(error).code)
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
}
