package rsanzjo.entreteclasytinta.template.domain.useCase

import kotlinx.coroutines.flow.Flow
import rsanzjo.entreteclasytinta.template.domain.entity.SessionState
import rsanzjo.entreteclasytinta.template.domain.entity.SocialProvider

interface AuthUseCase {
    val sessionState: Flow<SessionState>

    suspend fun login(email: String, password: String)

    suspend fun loginWith(provider: SocialProvider)

    /** @return `true` si la sesión queda iniciada; `false` si antes hay que confirmar el correo. */
    suspend fun register(email: String, password: String): Boolean

    suspend fun logout()

    suspend fun sendPasswordReset(email: String)

    suspend fun updatePassword(newPassword: String)
}
