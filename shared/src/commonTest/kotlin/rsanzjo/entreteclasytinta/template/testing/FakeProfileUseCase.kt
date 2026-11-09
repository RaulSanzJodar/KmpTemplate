package rsanzjo.entreteclasytinta.template.testing

import rsanzjo.entreteclasytinta.template.domain.entity.UserProfile
import rsanzjo.entreteclasytinta.template.domain.useCase.ProfileUseCase

/** [ProfileUseCase] configurable para los tests de ViewModel. */
class FakeProfileUseCase(
    var profile: UserProfile = UserProfile(id = "1", name = "Ada", email = "ada@example.com"),
    var error: Throwable? = null
) : ProfileUseCase {

    override suspend fun getCurrentProfile(): UserProfile {
        error?.let { throw it }
        return profile
    }
}
