package rsanzjo.entreteclasytinta.template.presentation.feature.auth.newpassword

import rsanzjo.entreteclasytinta.template.domain.useCase.AuthUseCase
import rsanzjo.entreteclasytinta.template.presentation.common.base.ViewModelBase
import rsanzjo.entreteclasytinta.template.presentation.common.base.toErrorMessageRes
import rsanzjo.entreteclasytinta.template.presentation.feature.auth.newpassword.NewPasswordAction.*

/**
 * Fija la contraseña tras abrir la app desde el enlace de recuperación. Al guardarla, la sesión
 * pasa a iniciada y `App` lleva a la zona privada.
 */
class NewPasswordViewModel(
    private val authUseCase: AuthUseCase
) : ViewModelBase<NewPasswordUiState, NewPasswordAction, NewPasswordEffect>(NewPasswordUiState()) {

    override fun manageAction(action: NewPasswordAction) {
        when (action) {
            is OnPasswordChange -> setState { copy(password = action.password) }
            is OnConfirmPasswordChange -> setState { copy(confirmPassword = action.confirmPassword) }
            OnSaveClick -> savePassword()
        }
    }

    private fun savePassword() {
        val state = currentState
        if (!state.isSaveEnabled || baseUiState.value.isLoading) return

        launchWithLoading(
            onError = { error -> sendEffect(NewPasswordEffect.ShowMessage(error.toErrorMessageRes())) }
        ) {
            authUseCase.updatePassword(state.password)
        }
    }
}
