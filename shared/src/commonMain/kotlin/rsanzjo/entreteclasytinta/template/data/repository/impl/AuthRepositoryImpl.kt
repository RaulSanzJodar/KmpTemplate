package rsanzjo.entreteclasytinta.template.data.repository.impl

import kotlinx.coroutines.flow.firstOrNull
import rsanzjo.entreteclasytinta.template.data.datasource.SessionManager
import rsanzjo.entreteclasytinta.template.data.datasource.api.AppApi
import rsanzjo.entreteclasytinta.template.data.datasource.api.request.LoginRequest
import rsanzjo.entreteclasytinta.template.data.datasource.api.request.LogoutRequest
import rsanzjo.entreteclasytinta.template.domain.repository.AuthRepository

class AuthRepositoryImpl(
    private val api: AppApi,
    private val sessionManager: SessionManager
) : AuthRepository {

    override suspend fun signIn(email: String, password: String) {
        val response = api.login(LoginRequest(email, password))
        if (!response.token.isNullOrEmpty()) {
            sessionManager.saveTokens(response.token, response.refreshToken)
        }
    }

    override suspend fun signOut() {
        val currentRefreshToken = sessionManager.refreshToken.firstOrNull()
        if (!currentRefreshToken.isNullOrEmpty()) {
            try {
                api.postLogout(LogoutRequest(currentRefreshToken))
            } catch (_: Exception) {
                // Ignore errors during logout request
            }
        }
        sessionManager.clearToken()
    }

    override suspend fun isSignedIn(): Boolean {
        return !sessionManager.token.firstOrNull().isNullOrEmpty()
    }
}
