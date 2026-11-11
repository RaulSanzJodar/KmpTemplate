package rsanzjo.entreteclasytinta.template.presentation.common.navigation.auth

import kotlinx.serialization.Serializable
import rsanzjo.entreteclasytinta.template.presentation.common.navigation.AppDestination

@Serializable
sealed class AuthDestination : AppDestination() {
    @Serializable
    data object LoginScreen : AuthDestination()

    @Serializable
    data object RegisterScreen : AuthDestination()

    @Serializable
    data object ForgotPasswordScreen : AuthDestination()

    /** Se llega desde el enlace de recuperación de contraseña (deep link), no desde otra pantalla. */
    @Serializable
    data object NewPasswordScreen : AuthDestination()
}
