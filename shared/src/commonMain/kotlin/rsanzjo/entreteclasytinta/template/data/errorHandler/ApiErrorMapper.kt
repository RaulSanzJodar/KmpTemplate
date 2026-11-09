package rsanzjo.entreteclasytinta.template.data.errorHandler

import io.ktor.client.plugins.ResponseException
import kotlinx.io.IOException
import rsanzjo.entreteclasytinta.template.domain.error.DomainError

/**
 * Traduce las excepciones de Ktor a [DomainError]. Las que no son de red ni HTTP (incluida la
 * cancelación de corrutinas) se devuelven tal cual.
 */
fun Throwable.toDomainError(): Throwable = when {
    this is DomainError -> this
    this is ResponseException -> when (response.status.value) {
        401 -> DomainError.Unauthorized(this)
        429 -> DomainError.TooManyRequests(this)
        else -> DomainError.Http(response.status.value, this)
    }
    this is IOException -> DomainError.Network(this)
    else -> this
}
