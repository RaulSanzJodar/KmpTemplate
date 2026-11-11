package rsanzjo.entreteclasytinta.template.data.errorHandler

import io.github.jan.supabase.auth.exception.AuthErrorCode
import io.github.jan.supabase.auth.exception.AuthRestException
import io.github.jan.supabase.exceptions.RestException
import io.ktor.client.plugins.ResponseException
import kotlinx.io.IOException
import rsanzjo.entreteclasytinta.template.domain.error.DomainError

/**
 * Traduce las excepciones de Supabase y de Ktor a [DomainError]. Las que no son de red ni HTTP
 * (incluida la cancelación de corrutinas) se devuelven tal cual.
 */
fun Throwable.toDomainError(): Throwable = when (this) {
    is DomainError -> this
    is AuthRestException -> toAuthDomainError()
    is RestException -> httpDomainError(statusCode, this)
    is ResponseException -> httpDomainError(response.status.value, this)
    // Incluye `HttpRequestException` (Supabase) y los timeouts de Ktor.
    is IOException -> DomainError.Network(this)
    else -> this
}

/** Ejecuta [block] y relanza sus errores como [DomainError]; todo repositorio envuelve así sus llamadas. */
suspend fun <T> domainCall(block: suspend () -> T): T =
    try {
        block()
    } catch (e: Throwable) {
        throw e.toDomainError()
    }

private fun AuthRestException.toAuthDomainError(): DomainError = when (errorCode) {
    AuthErrorCode.InvalidCredentials -> DomainError.InvalidCredentials(this)
    AuthErrorCode.EmailNotConfirmed -> DomainError.EmailNotConfirmed(this)
    AuthErrorCode.UserAlreadyExists, AuthErrorCode.EmailExists -> DomainError.UserAlreadyExists(this)
    AuthErrorCode.WeakPassword -> DomainError.WeakPassword(this)
    AuthErrorCode.OverRequestRateLimit, AuthErrorCode.OverEmailSendRateLimit -> DomainError.TooManyRequests(this)
    AuthErrorCode.SessionExpired,
    AuthErrorCode.SessionNotFound,
    AuthErrorCode.RefreshTokenNotFound,
    AuthErrorCode.RefreshTokenAlreadyUsed,
    AuthErrorCode.BadJwt -> DomainError.Unauthorized(this)
    else -> httpDomainError(statusCode, this)
}

private fun httpDomainError(status: Int, cause: Throwable): DomainError = when (status) {
    401 -> DomainError.Unauthorized(cause)
    429 -> DomainError.TooManyRequests(cause)
    else -> DomainError.Http(status, cause)
}
