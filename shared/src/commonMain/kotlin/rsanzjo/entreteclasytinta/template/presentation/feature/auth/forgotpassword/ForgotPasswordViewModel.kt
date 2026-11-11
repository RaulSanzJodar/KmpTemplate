package rsanzjo.entreteclasytinta.template.presentation.feature.auth.forgotpassword

import rsanzjo.entreteclasytinta.template.domain.useCase.AuthUseCase
import rsanzjo.entreteclasytinta.template.presentation.common.base.ViewModelBase
import rsanzjo.entreteclasytinta.template.presentation.common.base.toErrorMessageRes
import rsanzjo.entreteclasytinta.template.presentation.common.navigation.BackNavigation
import rsanzjo.entreteclasytinta.template.presentation.feature.auth.forgotpassword.ForgotPasswordAction.*
import rsanzjo.entreteclasytinta.template.resources.Res
import rsanzjo.entreteclasytinta.template.resources.forgot_password_sent

class ForgotPasswordViewModel(
    private val authUseCase: AuthUseCase
) : ViewModelBase<ForgotPasswordUiState, ForgotPasswordAction, ForgotPasswordEffect>(ForgotPasswordUiState()) {

    override fun manageAction(action: ForgotPasswordAction) {
        when (action) {
            is OnEmailChange -> setState { copy(email = action.email) }
            OnSendClick -> sendResetEmail()
            OnBackClick -> navigateTo(BackNavigation())
        }
    }

    private fun sendResetEmail() {
        val state = currentState
        if (!state.isSendEnabled || baseUiState.value.isLoading) return

        launchWithLoading(
            onError = { error -> sendEffect(ForgotPasswordEffect.ShowMessage(error.toErrorMessageRes())) }
        ) {
            authUseCase.sendPasswordReset(state.email)
            // El mismo mensaje exista o no la cuenta: no se revela qué correos están registrados.
            sendEffect(ForgotPasswordEffect.ShowMessage(Res.string.forgot_password_sent))
        }
    }
}
