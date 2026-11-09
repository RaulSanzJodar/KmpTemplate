package rsanzjo.entreteclasytinta.template.presentation.feature.auth.login

import org.jetbrains.compose.resources.StringResource

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isFormVisible: Boolean = false
) {
    val isLoginEnabled: Boolean get() = email.isNotBlank() && password.isNotBlank()
}

sealed class LoginAction {
    data object OnStart : LoginAction()
    data class OnEmailChange(val email: String) : LoginAction()
    data class OnPasswordChange(val password: String) : LoginAction()
    data object OnLoginClick : LoginAction()
}

sealed class LoginEffect {
    data class ShowMessage(val messageRes: StringResource) : LoginEffect()
}
