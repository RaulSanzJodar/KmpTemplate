package rsanzjo.entreteclasytinta.template.presentation.common.navigation.auth

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import rsanzjo.entreteclasytinta.template.presentation.common.navigation.handleNavEvent
import rsanzjo.entreteclasytinta.template.presentation.feature.auth.forgotpassword.ForgotPasswordScreen
import rsanzjo.entreteclasytinta.template.presentation.feature.auth.login.LoginScreen
import rsanzjo.entreteclasytinta.template.presentation.feature.auth.newpassword.NewPasswordScreen
import rsanzjo.entreteclasytinta.template.presentation.feature.auth.register.RegisterScreen

/** Destinos sin sesión iniciada. El paso a la zona privada lo hace `App` al cambiar la sesión. */
fun NavGraphBuilder.authGraph(navController: NavController) {
    composable<AuthDestination.LoginScreen> {
        LoginScreen(onNavEvent = { navController.handleNavEvent(it) })
    }
    composable<AuthDestination.RegisterScreen> {
        RegisterScreen(onNavEvent = { navController.handleNavEvent(it) })
    }
    composable<AuthDestination.ForgotPasswordScreen> {
        ForgotPasswordScreen(onNavEvent = { navController.handleNavEvent(it) })
    }
    composable<AuthDestination.NewPasswordScreen> {
        NewPasswordScreen()
    }
}
