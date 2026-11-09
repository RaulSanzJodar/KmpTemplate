package rsanzjo.entreteclasytinta.template.data.datasource.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import rsanzjo.entreteclasytinta.template.data.datasource.api.request.LoginRequest
import rsanzjo.entreteclasytinta.template.data.datasource.api.request.LogoutRequest
import rsanzjo.entreteclasytinta.template.data.datasource.api.response.LoginResponse
import rsanzjo.entreteclasytinta.template.data.datasource.api.response.UserProfileResponse

/** Cliente de la API REST sobre Ktor; los errores HTTP salen como `DomainError` (ver `createHttpClient`). */
class AppApi(private val client: HttpClient) {

    suspend fun login(request: LoginRequest): LoginResponse =
        client.post("login") { setBody(request) }.body()

    suspend fun postLogout(request: LogoutRequest) {
        client.post("logout") { setBody(request) }
    }

    suspend fun getProfile(): UserProfileResponse =
        client.get("profile").body()
}
