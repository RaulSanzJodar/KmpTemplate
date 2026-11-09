package rsanzjo.entreteclasytinta.template.testing

import rsanzjo.entreteclasytinta.template.domain.useCase.AuthUseCase

/** [AuthUseCase] configurable que registra las llamadas, para los tests de ViewModel. */
class FakeAuthUseCase(
    var loggedIn: Boolean = false,
    var loginError: Throwable? = null
) : AuthUseCase {
    var loginCalls = 0
    var logoutCalls = 0

    override suspend fun login(email: String, password: String) {
        loginCalls++
        loginError?.let { throw it }
    }

    override suspend fun logout() { logoutCalls++ }

    override suspend fun isLoggedIn(): Boolean = loggedIn
}
