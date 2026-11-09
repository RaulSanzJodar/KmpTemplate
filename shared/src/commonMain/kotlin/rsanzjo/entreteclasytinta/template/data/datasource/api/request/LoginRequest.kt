package rsanzjo.entreteclasytinta.template.data.datasource.api.request

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(val email: String, val password: String)
