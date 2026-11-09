package rsanzjo.entreteclasytinta.template.domain.repository

import rsanzjo.entreteclasytinta.template.domain.entity.UserProfile

interface ProfileRepository {
    suspend fun getCurrentProfile(): UserProfile
}
