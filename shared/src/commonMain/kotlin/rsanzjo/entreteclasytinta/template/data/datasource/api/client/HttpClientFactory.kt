package rsanzjo.entreteclasytinta.template.data.datasource.api.client

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.call.HttpClientCall
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ResponseException
import kotlin.coroutines.cancellation.CancellationException
import kotlinx.io.IOException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.plugins.HttpSend
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.plugin
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual
import rsanzjo.entreteclasytinta.template.common.Log
import rsanzjo.entreteclasytinta.template.data.datasource.SessionManager
import rsanzjo.entreteclasytinta.template.data.datasource.api.request.RefreshTokenRequest
import rsanzjo.entreteclasytinta.template.data.errorHandler.toDomainError
import rsanzjo.entreteclasytinta.template.data.datasource.api.response.RefreshTokenResponse

const val API_BASE_URL = "https://api.example.com/"

val ApiJson = Json {
    ignoreUnknownKeys = true
    isLenient = true
    coerceInputValues = true
    explicitNulls = false
    serializersModule = SerializersModule { contextual(Any::class, AnySerializer) }
}

/**
 * Cliente de la API ([engine] permite inyectar uno de prueba): añade el token de sesión a cada petición y, si la respuesta es 401, intenta
 * renovarlo una vez con el refresh token y reintenta la petición.
 */
fun createHttpClient(sessionManager: SessionManager, engine: HttpClientEngine? = null): HttpClient {
    val refreshClient = newClient(engine) {
        // Con expectSuccess un 4xx/5xx del refresh lanza ResponseException en vez de intentar leerlo.
        expectSuccess = true
        install(ContentNegotiation) { json(ApiJson) }
        defaultRequest { url(API_BASE_URL) }
    }
    val refreshMutex = Mutex()

    val client = newClient(engine) {
        // Sin `expectSuccess`: el 401 debe llegar al interceptor de HttpSend para poder renovar el
        // token, que es quien lanza el error HTTP (como hacía `HttpException` en Retrofit).
        install(ContentNegotiation) { json(ApiJson) }
        install(Logging) {
            // Sin cuerpos ni cabeceras: BODY registraría tokens y la contraseña del login.
            level = LogLevel.INFO
            logger = object : Logger {
                override fun log(message: String) = Log.d("Api", message)
            }
        }
        defaultRequest {
            url(API_BASE_URL)
            contentType(ContentType.Application.Json)
        }
    }

    client.plugin(HttpSend).intercept { request ->
        try {
            val sentToken = sessionManager.token.firstOrNull()
            if (!sentToken.isNullOrEmpty()) {
                request.header(HttpHeaders.Authorization, "Bearer $sentToken")
            }

            val call = execute(request)
            if (call.response.status != HttpStatusCode.Unauthorized) return@intercept call.throwIfError()

            val newToken = refreshMutex.withLock {
                val current = sessionManager.token.firstOrNull()
                if (!current.isNullOrEmpty() && current != sentToken) {
                    current
                } else {
                    refreshAccessToken(refreshClient, sessionManager)
                }
            } ?: return@intercept call.throwIfError()

            request.headers.remove(HttpHeaders.Authorization)
            request.header(HttpHeaders.Authorization, "Bearer $newToken")
            execute(request).throwIfError()
        } catch (e: Throwable) {
            // Presentation no conoce Ktor: las excepciones de red y HTTP salen como DomainError.
            throw e.toDomainError()
        }
    }

    return client
}

/** Crea el cliente con el motor de la plataforma, o con [engine] si se indica (tests). */
private fun newClient(engine: HttpClientEngine?, config: HttpClientConfig<*>.() -> Unit): HttpClient =
    if (engine != null) HttpClient(engine, config) else HttpClient(config)

private fun HttpClientCall.throwIfError(): HttpClientCall {
    when (response.status.value) {
        in 400..499 -> throw ClientRequestException(response, "")
        in 500..599 -> throw ServerResponseException(response, "")
    }
    return this
}

private suspend fun refreshAccessToken(refreshClient: HttpClient, sessionManager: SessionManager): String? {
    val refreshToken = sessionManager.refreshToken.firstOrNull()
    if (refreshToken.isNullOrEmpty()) {
        sessionManager.clearToken()
        return null
    }

    return try {
        val response: RefreshTokenResponse = refreshClient.post("refresh") {
            contentType(ContentType.Application.Json)
            setBody(RefreshTokenRequest(refreshToken))
        }.body()
        val newToken = response.token
        if (newToken.isNullOrEmpty()) {
            sessionManager.clearToken()
            null
        } else {
            sessionManager.saveTokens(newToken, response.refreshToken)
            newToken
        }
    } catch (e: CancellationException) {
        throw e
    } catch (e: ResponseException) {
        // El servidor rechazó el refresh (400/401): la sesión ya no es válida.
        if (e.response.status.value in 400..499) sessionManager.clearToken()
        null
    } catch (e: IOException) {
        // Sin red: se conservan los tokens para reintentar más tarde.
        null
    }
}
