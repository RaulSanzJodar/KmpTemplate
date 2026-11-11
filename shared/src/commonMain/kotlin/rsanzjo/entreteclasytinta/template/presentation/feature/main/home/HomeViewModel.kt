package rsanzjo.entreteclasytinta.template.presentation.feature.main.home

import rsanzjo.entreteclasytinta.template.domain.useCase.AuthUseCase
import rsanzjo.entreteclasytinta.template.domain.useCase.ProfileUseCase
import rsanzjo.entreteclasytinta.template.presentation.common.base.ViewModelBase

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

    // Sin navegación: al cerrarse la sesión, `App` vuelve sola a la zona de autenticación.
    private fun logout() {
        launchWithLoading {
            authUseCase.logout()
        }
    }
}
