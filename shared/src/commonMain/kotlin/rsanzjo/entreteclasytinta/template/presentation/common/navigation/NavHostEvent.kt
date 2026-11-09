package rsanzjo.entreteclasytinta.template.presentation.common.navigation

open class NavHostEvent

data class Navigation(val destination: AppDestination) : NavHostEvent()
data class BackNavigation(val toDestination: AppDestination? = null) : NavHostEvent()
data class NavigationInclusive(
    val destination: AppDestination,
    val popUpTo: AppDestination
) : NavHostEvent()
