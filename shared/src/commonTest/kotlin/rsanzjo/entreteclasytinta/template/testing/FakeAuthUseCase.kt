package rsanzjo.entreteclasytinta.template.testing

import kotlinx.coroutines.flow.MutableStateFlow
import rsanzjo.entreteclasytinta.template.domain.entity.SessionState
import rsanzjo.entreteclasytinta.template.domain.entity.SocialProvider
import rsanzjo.entreteclasytinta.template.domain.useCase.AuthUseCase

/** [AuthUseCase] configurable que registra las llamadas, para los tests de ViewModel. */
class FakeAuthUseCase(
    /** Error que lanza cualquier operación; `null` para que todas tengan éxito. */
    var error: Throwable? = null,
    /** Resultado de [register]: `false` simula que falta confirmar el correo. */
    var registerSignsIn: Boolean = true
) : AuthUseCase {
    override val sessionState = MutableStateFlow<SessionState>(SessionState.Unauthenticated())

    var loginCalls = 0
    var logoutCalls = 0
    val socialLogins = mutableListOf<SocialProvider>()
    val registeredEmails = mutableListOf<String>()
    val passwordResets = mutableListOf<String>()
    val updatedPasswords = mutableListOf<String>()

    override suspend fun login(email: String, password: String) {
        loginCalls++
        error?.let { throw it }
    }

    override suspend fun loginWith(provider: SocialProvider) {
        socialLogins += provider
        error?.let { throw it }
    }

    override suspend fun register(email: String, password: String): Boolean {
        registeredEmails += email
        error?.let { throw it }
        return registerSignsIn
    }

    override suspend fun logout() {
        logoutCalls++
        error?.let { throw it }
    }

    override suspend fun sendPasswordReset(email: String) {
        passwordResets += email
        error?.let { throw it }
    }

    override suspend fun updatePassword(newPassword: String) {
        updatedPasswords += newPassword
        error?.let { throw it }
    }
}
