package rsanzjo.entreteclasytinta.template.presentation.feature.auth.register

import rsanzjo.entreteclasytinta.template.domain.useCase.AuthUseCase
import rsanzjo.entreteclasytinta.template.presentation.common.base.ViewModelBase
import rsanzjo.entreteclasytinta.template.presentation.common.base.toErrorMessageRes
import rsanzjo.entreteclasytinta.template.presentation.common.navigation.BackNavigation
import rsanzjo.entreteclasytinta.template.presentation.feature.auth.register.RegisterAction.*
import rsanzjo.entreteclasytinta.template.resources.Res
import rsanzjo.entreteclasytinta.template.resources.register_confirmation_sent

class RegisterViewModel(
    private val authUseCase: AuthUseCase
) : ViewModelBase<RegisterUiState, RegisterAction, RegisterEffect>(RegisterUiState()) {

    override fun manageAction(action: RegisterAction) {
        when (action) {
            is OnEmailChange -> setState { copy(email = action.email) }
            is OnPasswordChange -> setState { copy(password = action.password) }
            is OnConfirmPasswordChange -> setState { copy(confirmPassword = action.confirmPassword) }
            OnRegisterClick -> register()
            OnBackClick -> navigateTo(BackNavigation())
        }
    }

    private fun register() {
        val state = currentState
        if (!state.isRegisterEnabled || baseUiState.value.isLoading) return

        launchWithLoading(
            onError = { error -> sendEffect(RegisterEffect.ShowMessage(error.toErrorMessageRes())) }
        ) {
            val isSignedIn = authUseCase.register(state.email, state.password)
            // Con sesión iniciada, `App` pasa sola a la zona privada. Si el proyecto exige
            // confirmar el correo, se avisa y se deja el formulario limpio.
            if (!isSignedIn) {
                setState { RegisterUiState() }
                sendEffect(RegisterEffect.ShowMessage(Res.string.register_confirmation_sent))
            }
        }
    }
}
