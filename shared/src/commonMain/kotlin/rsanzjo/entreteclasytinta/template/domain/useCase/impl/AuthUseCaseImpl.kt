package rsanzjo.entreteclasytinta.template.domain.useCase.impl

import kotlinx.coroutines.flow.Flow
import rsanzjo.entreteclasytinta.template.domain.entity.SessionState
import rsanzjo.entreteclasytinta.template.domain.entity.SocialProvider
import rsanzjo.entreteclasytinta.template.domain.repository.AuthRepository
import rsanzjo.entreteclasytinta.template.domain.useCase.AuthUseCase

class AuthUseCaseImpl(private val authRepository: AuthRepository) : AuthUseCase {

    override val sessionState: Flow<SessionState> get() = authRepository.sessionState

    override suspend fun login(email: String, password: String) = authRepository.signIn(email.trim(), password)

    override suspend fun loginWith(provider: SocialProvider) = authRepository.signInWith(provider)

    override suspend fun register(email: String, password: String): Boolean =
        authRepository.signUp(email.trim(), password)

    override suspend fun logout() = authRepository.signOut()

    override suspend fun sendPasswordReset(email: String) = authRepository.sendPasswordReset(email.trim())

    override suspend fun updatePassword(newPassword: String) = authRepository.updatePassword(newPassword)
}
