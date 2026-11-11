package rsanzjo.entreteclasytinta.template.data.datasource.supabase

import io.github.jan.supabase.auth.status.SessionSource
import io.github.jan.supabase.auth.status.SessionStatus
import rsanzjo.entreteclasytinta.template.domain.entity.SessionState

private const val RECOVERY_SESSION_TYPE = "recovery"

/**
 * Traduce el estado de sesión de Supabase al del dominio.
 *
 * @param previous último [SessionState] emitido; hace falta porque Supabase vuelve a `Initializing`
 * cada vez que la app regresa de segundo plano y eso no debe verse como una carga nueva.
 * @param signOutRequested si el cierre de sesión lo pidió el usuario: Supabase marca igual un
 * cierre voluntario que una sesión revocada.
 */
internal fun SessionStatus.toSessionState(previous: SessionState, signOutRequested: Boolean): SessionState =
    when (this) {
        SessionStatus.Initializing -> previous

        is SessionStatus.Authenticated ->
            if (source is SessionSource.External && session.type == RECOVERY_SESSION_TYPE) {
                SessionState.PasswordRecovery
            } else {
                SessionState.Authenticated
            }

        // Token caducado y sin poder renovarlo por red: la sesión sigue existiendo y Supabase reintenta.
        is SessionStatus.RefreshFailure ->
            if (previous is SessionState.PasswordRecovery) previous else SessionState.Authenticated

        is SessionStatus.NotAuthenticated -> SessionState.Unauthenticated(
            expired = !signOutRequested &&
                (previous is SessionState.Authenticated || previous is SessionState.PasswordRecovery)
        )
    }
