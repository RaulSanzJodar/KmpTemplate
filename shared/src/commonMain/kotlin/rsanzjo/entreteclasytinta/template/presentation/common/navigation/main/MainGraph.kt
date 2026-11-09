package rsanzjo.entreteclasytinta.template.presentation.common.navigation.main

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import rsanzjo.entreteclasytinta.template.presentation.common.navigation.handleNavEvent
import rsanzjo.entreteclasytinta.template.presentation.feature.main.home.HomeScreen

/** Destinos de la zona autenticada; cada pantalla nueva se registra aquí con `composable<...>`. */
fun NavGraphBuilder.mainGraph(navController: NavController) {
    composable<MainDestination.HomeScreen> {
        HomeScreen(onNavEvent = { navController.handleNavEvent(it) })
    }
}
