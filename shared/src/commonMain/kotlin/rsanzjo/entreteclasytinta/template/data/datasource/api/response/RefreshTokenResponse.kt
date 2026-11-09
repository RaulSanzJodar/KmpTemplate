package rsanzjo.entreteclasytinta.template.data.datasource.api.response

import kotlinx.serialization.Serializable

@Serializable
data class RefreshTokenResponse(
    val token: String? = null,
    val refreshToken: String? = null,
    val error: String? = null
)
