package rsanzjo.entreteclasytinta.template.domain.repository

import kotlinx.coroutines.flow.Flow
import rsanzjo.entreteclasytinta.template.domain.entity.SessionState
import rsanzjo.entreteclasytinta.template.domain.entity.SocialProvider

interface AuthRepository {
    /** Estado de la sesión; emite cada vez que se inicia, se cierra o caduca. */
    val sessionState: Flow<SessionState>

    suspend fun signIn(email: String, password: String)

    /** Abre el navegador con el proveedor; la sesión llega después por deep link. */
    suspend fun signInWith(provider: SocialProvider)

    /** @return `true` si la sesión queda iniciada; `false` si antes hay que confirmar el correo. */
    suspend fun signUp(email: String, password: String): Boolean

    suspend fun signOut()

    /** Envía el correo con el enlace de recuperación (abre la app en [SessionState.PasswordRecovery]). */
    suspend fun sendPasswordReset(email: String)

    /** Cambia la contraseña del usuario con sesión iniciada. */
    suspend fun updatePassword(newPassword: String)
}
