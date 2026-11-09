package rsanzjo.entreteclasytinta.template.presentation.common.navigation.auth

import kotlinx.serialization.Serializable
import rsanzjo.entreteclasytinta.template.presentation.common.navigation.AppDestination

@Serializable
sealed class AuthDestination : AppDestination() {
    @Serializable
    data object LoginScreen : AuthDestination()
}
