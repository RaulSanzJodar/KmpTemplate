package rsanzjo.entreteclasytinta.template.presentation.feature.main.home

import rsanzjo.entreteclasytinta.template.domain.useCase.AuthUseCase
import rsanzjo.entreteclasytinta.template.domain.useCase.ProfileUseCase
import rsanzjo.entreteclasytinta.template.presentation.common.base.ViewModelBase
import rsanzjo.entreteclasytinta.template.presentation.common.navigation.NavigationInclusive
import rsanzjo.entreteclasytinta.template.presentation.common.navigation.auth.AuthDestination
import rsanzjo.entreteclasytinta.template.presentation.common.navigation.main.MainDestination

class HomeViewModel(
    private val profileUseCase: ProfileUseCase,
    private val authUseCase: AuthUseCase
) : ViewModelBase<HomeUiState, HomeAction, Nothing>(HomeUiState()) {

    override fun manageAction(action: HomeAction) {
        when (action) {
            HomeAction.OnStart -> loadProfile()
            HomeAction.OnLogoutClick -> logout()
        }
    }

    private fun loadProfile() {
        launchWithLoading {
            val profile = profileUseCase.getCurrentProfile()
            setState { copy(profile = profile) }
        }
    }

    private fun logout() {
        launchWithLoading {
            authUseCase.logout()
            navigateTo(
                NavigationInclusive(
                    destination = AuthDestination.LoginScreen,
                    popUpTo = MainDestination.HomeScreen
                )
            )
        }
    }
}
