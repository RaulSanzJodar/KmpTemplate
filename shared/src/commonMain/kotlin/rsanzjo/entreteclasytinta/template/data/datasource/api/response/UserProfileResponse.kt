package rsanzjo.entreteclasytinta.template.data.datasource.api.response

import kotlinx.serialization.Serializable
import rsanzjo.entreteclasytinta.template.domain.entity.UserProfile

@Serializable
data class UserProfileResponse(
    val id: String? = null,
    val name: String? = null,
    val email: String? = null
)

fun UserProfileResponse.toDomain() = UserProfile(
    id = id.orEmpty(),
    name = name.orEmpty(),
    email = email.orEmpty()
)
