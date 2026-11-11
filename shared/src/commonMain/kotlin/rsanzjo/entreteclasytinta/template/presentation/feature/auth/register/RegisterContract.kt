package rsanzjo.entreteclasytinta.template.presentation.feature.auth.register

import org.jetbrains.compose.resources.StringResource
import rsanzjo.entreteclasytinta.template.common.extensions.isValidEmail
import rsanzjo.entreteclasytinta.template.presentation.common.auth.MIN_PASSWORD_LENGTH

data class RegisterUiState(
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = ""
) {
    /** Solo se avisa cuando ya se ha escrito algo en la confirmación. */
    val passwordsMismatch: Boolean get() = confirmPassword.isNotEmpty() && confirmPassword != password

    val isRegisterEnabled: Boolean
        get() = email.trim().isValidEmail() && password.length >= MIN_PASSWORD_LENGTH && confirmPassword == password
}

sealed class RegisterAction {
    data class OnEmailChange(val email: String) : RegisterAction()
    data class OnPasswordChange(val password: String) : RegisterAction()
    data class OnConfirmPasswordChange(val confirmPassword: String) : RegisterAction()
    data object OnRegisterClick : RegisterAction()
    data object OnBackClick : RegisterAction()
}

sealed class RegisterEffect {
    data class ShowMessage(val messageRes: StringResource) : RegisterEffect()
}
