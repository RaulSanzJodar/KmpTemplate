package rsanzjo.entreteclasytinta.template.presentation.feature.auth.login

import rsanzjo.entreteclasytinta.template.domain.useCase.AuthUseCase
import rsanzjo.entreteclasytinta.template.presentation.common.base.ViewModelBase
import rsanzjo.entreteclasytinta.template.presentation.common.base.toErrorMessageRes
import rsanzjo.entreteclasytinta.template.presentation.common.navigation.NavigationInclusive
import rsanzjo.entreteclasytinta.template.presentation.common.navigation.auth.AuthDestination
import rsanzjo.entreteclasytinta.template.presentation.common.navigation.main.MainDestination
import rsanzjo.entreteclasytinta.template.presentation.feature.auth.login.LoginAction.*
import rsanzjo.entreteclasytinta.template.resources.Res
import rsanzjo.entreteclasytinta.template.resources.auth_invalid_credential

class LoginViewModel(
    private val authUseCase: AuthUseCase
) : ViewModelBase<LoginUiState, LoginAction, LoginEffect>(LoginUiState()) {

    private var isSessionChecked = false

    override fun manageAction(action: LoginAction) {
        when (action) {
            OnStart -> checkIfUserIsLoggedIn()
            is OnEmailChange -> setState { copy(email = action.email) }
            is OnPasswordChange -> setState { copy(password = action.password) }
            OnLoginClick -> authenticate()
        }
    }

    private fun checkIfUserIsLoggedIn() {
        if (isSessionChecked) return
        isSessionChecked = true

        launchWithLoading(onError = { setState { copy(isFormVisible = true) } }) {
            if (authUseCase.isLoggedIn()) {
                navigateToHome()
            } else {
                setState { copy(isFormVisible = true) }
            }
        }
    }

    private fun authenticate() {
        val state = currentState
        if (!state.isLoginEnabled || baseUiState.value.isLoading) return

        launchWithLoading(
            onError = { error ->
                sendEffect(LoginEffect.ShowMessage(error.toErrorMessageRes(unauthorized = Res.string.auth_invalid_credential)))
            }
        ) {
            authUseCase.login(state.email, state.password)
            navigateToHome()
        }
    }

    private fun navigateToHome() {
        setState { copy(isFormVisible = false) }
        navigateTo(
            NavigationInclusive(
                destination = MainDestination.HomeScreen,
                popUpTo = AuthDestination.LoginScreen
            )
        )
    }
}
