package rsanzjo.entreteclasytinta.template.presentation.common.navigation

import androidx.navigation.NavController
import rsanzjo.entreteclasytinta.template.common.extensions.navigateInclusive

/**
 * Traduce un [NavHostEvent] emitido por un ViewModel en una operación sobre el [NavController].
 *
 * @param onBack por defecto `popBackStack()`; sobrescribible para grafos con una política de vuelta propia.
 * @param onInclusive por defecto navega limpiando la pila hasta `popUpTo`; un grafo anidado puede delegarlo al padre.
 */
fun NavController.handleNavEvent(
    event: NavHostEvent,
    onBack: () -> Unit = { popBackStack() },
    onInclusive: (NavigationInclusive) -> Unit = {
        navigateInclusive(destination = it.destination, popUpTo = it.popUpTo)
    }
) {
    when (event) {
        is Navigation -> navigate(route = event.destination)
        is BackNavigation -> onBack()
        is NavigationInclusive -> onInclusive(event)
    }
}
