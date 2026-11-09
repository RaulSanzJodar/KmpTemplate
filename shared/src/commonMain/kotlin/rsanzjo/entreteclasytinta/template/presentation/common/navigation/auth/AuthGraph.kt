package rsanzjo.entreteclasytinta.template.presentation.common.navigation.auth

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import rsanzjo.entreteclasytinta.template.common.extensions.navigateToBack
import rsanzjo.entreteclasytinta.template.presentation.common.navigation.handleNavEvent
import rsanzjo.entreteclasytinta.template.presentation.feature.auth.login.LoginScreen

fun NavGraphBuilder.authGraph(navController: NavController) {
    composable<AuthDestination.LoginScreen> {
        LoginScreen(
            onNavEvent = { navController.handleNavEvent(it, onBack = { navController.navigateToBack() }) }
        )
    }
}
