package rsanzjo.entreteclasytinta.template.domain.error

/**
 * Errores de acceso a datos que la UI sabe interpretar. La capa de datos traduce las excepciones
 * del backend (Supabase, Ktor) a este tipo, de modo que presentation no depende de sus librerías.
 */
sealed class DomainError(message: String?, cause: Throwable?) : Exception(message, cause) {
    /** Sin conexión, timeout o corte de red. */
    class Network(cause: Throwable? = null) : DomainError("Network error", cause)

    /** 401: la sesión ha caducado o ya no es válida. */
    class Unauthorized(cause: Throwable? = null) : DomainError("Unauthorized", cause)

    /** Correo o contraseña incorrectos al iniciar sesión. */
    class InvalidCredentials(cause: Throwable? = null) : DomainError("Invalid credentials", cause)

    /** La cuenta existe pero aún no ha confirmado su correo. */
    class EmailNotConfirmed(cause: Throwable? = null) : DomainError("Email not confirmed", cause)

    /** Ya hay una cuenta registrada con ese correo. */
    class UserAlreadyExists(cause: Throwable? = null) : DomainError("User already exists", cause)

    /** La contraseña no cumple la política del proyecto. */
    class WeakPassword(cause: Throwable? = null) : DomainError("Weak password", cause)

    /** 429: demasiadas peticiones. */
    class TooManyRequests(cause: Throwable? = null) : DomainError("Too many requests", cause)

    /** Cualquier otra respuesta HTTP de error. */
    class Http(val code: Int, cause: Throwable? = null) : DomainError("HTTP $code", cause)
}
