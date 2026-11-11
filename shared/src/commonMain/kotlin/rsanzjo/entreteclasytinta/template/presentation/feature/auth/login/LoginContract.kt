package rsanzjo.entreteclasytinta.template.presentation.feature.auth.login

import org.jetbrains.compose.resources.StringResource
import rsanzjo.entreteclasytinta.template.domain.entity.SocialProvider

data class LoginUiState(
    val email: String = "",
    val password: String = ""
) {
    val isLoginEnabled: Boolean get() = email.isNotBlank() && password.isNotBlank()
}

sealed class LoginAction {
    data class OnEmailChange(val email: String) : LoginAction()
    data class OnPasswordChange(val password: String) : LoginAction()
    data object OnLoginClick : LoginAction()
    data object OnRegisterClick : LoginAction()
    data object OnForgotPasswordClick : LoginAction()

    /** Inicio de sesión social por navegador (OAuth). */
    data class OnSocialClick(val provider: SocialProvider) : LoginAction()

    /** El inicio de sesión nativo de Google ha fallado (lo gestiona la vista). */
    data object OnNativeSignInError : LoginAction()
}

sealed class LoginEffect {
    data class ShowMessage(val messageRes: StringResource) : LoginEffect()
}
