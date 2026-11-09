package rsanzjo.entreteclasytinta.template.presentation.common.navigation.main

import kotlinx.serialization.Serializable
import rsanzjo.entreteclasytinta.template.presentation.common.navigation.AppDestination

@Serializable
sealed class MainDestination : AppDestination() {
    @Serializable
    data object HomeScreen : MainDestination()
}
