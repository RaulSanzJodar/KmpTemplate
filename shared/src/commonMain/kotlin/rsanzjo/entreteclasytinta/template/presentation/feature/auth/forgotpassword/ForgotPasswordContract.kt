package rsanzjo.entreteclasytinta.template.presentation.feature.auth.forgotpassword

import org.jetbrains.compose.resources.StringResource
import rsanzjo.entreteclasytinta.template.common.extensions.isValidEmail

data class ForgotPasswordUiState(
    val email: String = ""
) {
    val isSendEnabled: Boolean get() = email.trim().isValidEmail()
}

sealed class ForgotPasswordAction {
    data class OnEmailChange(val email: String) : ForgotPasswordAction()
    data object OnSendClick : ForgotPasswordAction()
    data object OnBackClick : ForgotPasswordAction()
}

sealed class ForgotPasswordEffect {
    data class ShowMessage(val messageRes: StringResource) : ForgotPasswordEffect()
}
