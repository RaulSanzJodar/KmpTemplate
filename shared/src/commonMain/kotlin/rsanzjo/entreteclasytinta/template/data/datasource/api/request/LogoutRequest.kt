package rsanzjo.entreteclasytinta.template.data.datasource.api.request

import kotlinx.serialization.Serializable

@Serializable
data class LogoutRequest(
    val refreshToken: String
)
