package rsanzjo.entreteclasytinta.template.presentation.feature.auth.newpassword

import org.jetbrains.compose.resources.StringResource
import rsanzjo.entreteclasytinta.template.presentation.common.auth.MIN_PASSWORD_LENGTH

data class NewPasswordUiState(
    val password: String = "",
    val confirmPassword: String = ""
) {
    /** Solo se avisa cuando ya se ha escrito algo en la confirmación. */
    val passwordsMismatch: Boolean get() = confirmPassword.isNotEmpty() && confirmPassword != password

    val isSaveEnabled: Boolean get() = password.length >= MIN_PASSWORD_LENGTH && confirmPassword == password
}

sealed class NewPasswordAction {
    data class OnPasswordChange(val password: String) : NewPasswordAction()
    data class OnConfirmPasswordChange(val confirmPassword: String) : NewPasswordAction()
    data object OnSaveClick : NewPasswordAction()
}

sealed class NewPasswordEffect {
    data class ShowMessage(val messageRes: StringResource) : NewPasswordEffect()
}
