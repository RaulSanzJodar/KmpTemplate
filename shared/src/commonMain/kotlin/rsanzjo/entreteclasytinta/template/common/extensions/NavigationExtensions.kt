package rsanzjo.entreteclasytinta.template.common.extensions

import rsanzjo.entreteclasytinta.template.common.Log
import androidx.navigation.NavController
import rsanzjo.entreteclasytinta.template.presentation.common.navigation.AppDestination
import rsanzjo.entreteclasytinta.template.presentation.common.navigation.Navigation

fun NavController.navigateInclusive(destination: AppDestination, popUpTo: AppDestination) {
    this.navigate(destination) {
        popUpTo(popUpTo) {
            inclusive = true
        }
    }
}

fun NavController.navigateToEvent(event: Navigation, onParentNavigation: (Navigation) -> Unit) {
    try {
        navigate(event.destination)
    } catch (exception: Exception) {
        Log.e(
            "NavController",
            "Destination ${event.destination} not found in nav controller. Proceed to navigate in parent",
        )
        onParentNavigation(event)
    }
}

fun NavController.navigateToBack(destination: AppDestination? = null, inclusive: Boolean = true) {
    destination?.let { popBackStack(it, inclusive) } ?: popBackStack()
}

