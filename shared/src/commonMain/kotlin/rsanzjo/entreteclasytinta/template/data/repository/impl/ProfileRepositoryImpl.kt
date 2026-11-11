package rsanzjo.entreteclasytinta.template.data.repository.impl

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from
import rsanzjo.entreteclasytinta.template.data.datasource.supabase.dto.ProfileDto
import rsanzjo.entreteclasytinta.template.data.datasource.supabase.dto.toDomain
import rsanzjo.entreteclasytinta.template.data.errorHandler.domainCall
import rsanzjo.entreteclasytinta.template.domain.entity.UserProfile
import rsanzjo.entreteclasytinta.template.domain.error.DomainError
import rsanzjo.entreteclasytinta.template.domain.repository.ProfileRepository

class ProfileRepositoryImpl(private val supabase: SupabaseClient) : ProfileRepository {

    override suspend fun getCurrentProfile(): UserProfile = domainCall {
        val user = supabase.auth.currentUserOrNull() ?: throw DomainError.Unauthorized()

        // RLS ya limita la consulta a la fila propia; el filtro lo deja explícito.
        val profile = supabase.from(PROFILES_TABLE)
            .select { filter { eq("id", user.id) } }
            .decodeSingleOrNull<ProfileDto>()

        // Sin fila (p. ej. usuario anterior a la migración) se muestra al menos el correo de la cuenta.
        profile?.toDomain() ?: UserProfile(id = user.id, email = user.email.orEmpty())
    }

    private companion object {
        const val PROFILES_TABLE = "profiles"
    }
}
