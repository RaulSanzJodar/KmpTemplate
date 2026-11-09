package rsanzjo.entreteclasytinta.template.data.datasource

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import rsanzjo.entreteclasytinta.template.common.platform.SecureStorage

class SessionManager(private val storage: SecureStorage) {

    private val _token = MutableStateFlow(storage.getString(TOKEN_KEY))
    private val _refreshToken = MutableStateFlow(storage.getString(REFRESH_TOKEN_KEY))

    val token: Flow<String?> = _token.asStateFlow()
    val refreshToken: Flow<String?> = _refreshToken.asStateFlow()

    suspend fun saveToken(token: String) {
        storage.putString(TOKEN_KEY, token)
        _token.value = token
    }

    suspend fun saveRefreshToken(refreshToken: String) {
        storage.putString(REFRESH_TOKEN_KEY, refreshToken)
        _refreshToken.value = refreshToken
    }

    suspend fun saveTokens(token: String, refreshToken: String?) {
        saveToken(token)
        if (refreshToken != null) {
            saveRefreshToken(refreshToken)
        } else {
            storage.remove(REFRESH_TOKEN_KEY)
            _refreshToken.value = null
        }
    }

    suspend fun clearToken() {
        storage.remove(TOKEN_KEY)
        storage.remove(REFRESH_TOKEN_KEY)
        _token.value = null
        _refreshToken.value = null
    }

    private companion object {
        const val TOKEN_KEY = "access_token"
        const val REFRESH_TOKEN_KEY = "refresh_token"
    }
}
