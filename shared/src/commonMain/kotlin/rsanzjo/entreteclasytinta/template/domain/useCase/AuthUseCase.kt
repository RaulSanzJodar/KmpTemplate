package rsanzjo.entreteclasytinta.template.domain.useCase

interface AuthUseCase {
    suspend fun login(email: String, password: String)
    suspend fun logout()
    suspend fun isLoggedIn(): Boolean
}