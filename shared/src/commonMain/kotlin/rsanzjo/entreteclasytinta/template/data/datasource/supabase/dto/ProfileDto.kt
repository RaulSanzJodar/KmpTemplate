package rsanzjo.entreteclasytinta.template.data.datasource.supabase.dto

import kotlinx.serialization.Serializable
import rsanzjo.entreteclasytinta.template.domain.entity.UserProfile

/** Fila de la tabla `profiles` (ver `supabase/migrations`). */
@Serializable
data class ProfileDto(
    val id: String,
    val name: String? = null,
    val email: String? = null
)

fun ProfileDto.toDomain() = UserProfile(
    id = id,
    name = name.orEmpty(),
    email = email.orEmpty()
)
