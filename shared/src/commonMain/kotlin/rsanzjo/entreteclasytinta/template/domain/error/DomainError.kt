package rsanzjo.entreteclasytinta.template.domain.error

/**
 * Errores de acceso a datos que la UI sabe interpretar. La capa de datos traduce las excepciones
 * de red (Ktor) a este tipo, de modo que presentation no depende de la librería HTTP.
 */
sealed class DomainError(message: String?, cause: Throwable?) : Exception(message, cause) {
    /** Sin conexión, timeout o corte de red. */
    class Network(cause: Throwable? = null) : DomainError("Network error", cause)

    /** 401: sesión caducada o credenciales incorrectas. */
    class Unauthorized(cause: Throwable? = null) : DomainError("Unauthorized", cause)

    /** 429: demasiadas peticiones. */
    class TooManyRequests(cause: Throwable? = null) : DomainError("Too many requests", cause)

    /** Cualquier otra respuesta HTTP de error. */
    class Http(val code: Int, cause: Throwable? = null) : DomainError("HTTP $code", cause)
}
