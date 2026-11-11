package rsanzjo.entreteclasytinta.template.domain.entity

/** Estado de la sesión del usuario; la navegación raíz (`App`) se guía por él. */
sealed interface SessionState {
    /** Aún no se sabe si hay una sesión guardada. */
    data object Loading : SessionState

    data object Authenticated : SessionState

    /** @param expired la sesión se perdió sin que el usuario la cerrase (caducada o revocada). */
    data class Unauthenticated(val expired: Boolean = false) : SessionState

    /** Sesión abierta desde un enlace de recuperación: falta fijar la nueva contraseña. */
    data object PasswordRecovery : SessionState
}
