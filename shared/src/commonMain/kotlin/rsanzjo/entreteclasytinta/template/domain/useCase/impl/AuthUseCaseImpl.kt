package rsanzjo.entreteclasytinta.template.domain.useCase.impl

import rsanzjo.entreteclasytinta.template.domain.repository.AuthRepository
import rsanzjo.entreteclasytinta.template.domain.useCase.AuthUseCase

class AuthUseCaseImpl(val authRepository: AuthRepository) : AuthUseCase {

    override suspend fun login(email: String, password: String) {
        authRepository.signIn(email, password)
    }

    override suspend fun logout() {
        authRepository.signOut()
    }

    override suspend fun isLoggedIn(): Boolean {
        return authRepository.isSignedIn()
    }
}