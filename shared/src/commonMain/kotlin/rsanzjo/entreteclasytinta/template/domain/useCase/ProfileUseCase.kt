package rsanzjo.entreteclasytinta.template.domain.useCase

import rsanzjo.entreteclasytinta.template.domain.entity.UserProfile

interface ProfileUseCase {
    suspend fun getCurrentProfile(): UserProfile
}
