package rsanzjo.entreteclasytinta.template.presentation.feature.auth.login

import rsanzjo.entreteclasytinta.template.domain.entity.SocialProvider
import rsanzjo.entreteclasytinta.template.domain.useCase.AuthUseCase
import rsanzjo.entreteclasytinta.template.presentation.common.base.ViewModelBase
import rsanzjo.entreteclasytinta.template.presentation.common.base.toErrorMessageRes
import rsanzjo.entreteclasytinta.template.presentation.common.navigation.Navigation
import rsanzjo.entreteclasytinta.template.presentation.common.navigation.auth.AuthDestination
import rsanzjo.entreteclasytinta.template.presentation.feature.auth.login.LoginAction.*
import rsanzjo.entreteclasytinta.template.resources.Res
import rsanzjo.entreteclasytinta.template.resources.error_unknown

/** Tras iniciar sesión no navega: `App` cambia de zona al actualizarse el estado de la sesión. */
class LoginViewModel(
    private val authUseCase: AuthUseCase
) : ViewModelBase<LoginUiState, LoginAction, LoginEffect>(LoginUiState()) {

    override fun manageAction(action: LoginAction) {
        when (action) {
            is OnEmailChange -> setState { copy(email = action.email) }
            is OnPasswordChange -> setState { copy(password = action.password) }
            OnLoginClick -> authenticate()
            OnRegisterClick -> navigateTo(Navigation(AuthDestination.RegisterScreen))
            OnForgotPasswordClick -> navigateTo(Navigation(AuthDestination.ForgotPasswordScreen))
            is OnSocialClick -> authenticateWith(action.provider)
            OnNativeSignInError -> sendEffect(LoginEffect.ShowMessage(Res.string.error_unknown))
        }
    }

    private fun authenticate() {
        val state = currentState
        if (!state.isLoginEnabled || baseUiState.value.isLoading) return

        launchWithLoading(onError = ::showError) {
            authUseCase.login(state.email, state.password)
        }
    }

    private fun authenticateWith(provider: SocialProvider) {
        if (baseUiState.value.isLoading) return

        launchWithLoading(onError = ::showError) {
            authUseCase.loginWith(provider)
        }
    }

    private fun showError(error: Throwable) {
        sendEffect(LoginEffect.ShowMessage(error.toErrorMessageRes()))
    }
}
