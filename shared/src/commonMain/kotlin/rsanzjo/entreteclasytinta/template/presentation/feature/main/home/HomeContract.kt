package rsanzjo.entreteclasytinta.template.presentation.feature.main.home

import rsanzjo.entreteclasytinta.template.domain.entity.UserProfile

data class HomeUiState(
    val profile: UserProfile? = null
)

sealed class HomeAction {
    data object OnStart : HomeAction()
    data object OnLogoutClick : HomeAction()
}
