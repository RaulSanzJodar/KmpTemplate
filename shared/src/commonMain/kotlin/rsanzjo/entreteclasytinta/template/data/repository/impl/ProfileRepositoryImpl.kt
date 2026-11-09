package rsanzjo.entreteclasytinta.template.data.repository.impl

import rsanzjo.entreteclasytinta.template.data.datasource.api.AppApi
import rsanzjo.entreteclasytinta.template.data.datasource.api.response.toDomain
import rsanzjo.entreteclasytinta.template.domain.entity.UserProfile
import rsanzjo.entreteclasytinta.template.domain.repository.ProfileRepository

class ProfileRepositoryImpl(private val api: AppApi) : ProfileRepository {

    override suspend fun getCurrentProfile(): UserProfile = api.getProfile().toDomain()
}
