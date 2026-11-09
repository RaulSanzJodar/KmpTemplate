package rsanzjo.entreteclasytinta.template.domain.useCase.impl

import rsanzjo.entreteclasytinta.template.domain.entity.UserProfile
import rsanzjo.entreteclasytinta.template.domain.repository.ProfileRepository
import rsanzjo.entreteclasytinta.template.domain.useCase.ProfileUseCase

class ProfileUseCaseImpl(private val repository: ProfileRepository) : ProfileUseCase {

    override suspend fun getCurrentProfile(): UserProfile = repository.getCurrentProfile()
}
