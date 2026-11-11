package rsanzjo.entreteclasytinta.template.testing

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import rsanzjo.entreteclasytinta.template.common.platform.SecureStorage
import rsanzjo.entreteclasytinta.template.data.datasource.supabase.createAppSupabaseClient

/** Respuesta de `token` y `signup` de Supabase Auth con una sesión válida. */
const val SESSION_JSON =
    """{"access_token":"access","refresh_token":"refresh","expires_in":3600,"token_type":"bearer",""" +
        """"user":{"id":"user-1","aud":"authenticated","email":"ada@example.com"}}"""

/** Cuerpo de error de Supabase Auth con el código [errorCode]. */
fun authErrorJson(errorCode: String) = """{"code":400,"error_code":"$errorCode","msg":"$errorCode"}"""

/**
 * Cliente de Supabase contra un servidor simulado: [handler] recibe la ruta de cada petición
 * (p. ej. `auth/v1/token`) y devuelve el estado y el cuerpo JSON de la respuesta.
 */
suspend fun testSupabaseClient(
    storage: SecureStorage = FakeSecureStorage(),
    handler: (path: String) -> Pair<HttpStatusCode, String>
): SupabaseClient {
    val engine = MockEngine { request ->
        val (status, body) = handler(request.url.encodedPath.trimStart('/'))
        respond(body, status, headersOf(HttpHeaders.ContentType, "application/json"))
    }
    val client = createAppSupabaseClient(
        storage = storage,
        url = "https://test.supabase.co",
        key = "test-key",
        engine = engine,
        platformPlugins = false
    ) {
        // Sin ciclo de vida de Android ni refresco en segundo plano durante los tests.
        enableLifecycleCallbacks = false
        alwaysAutoRefresh = false
    }
    // La sesión guardada se carga en segundo plano al crear el cliente.
    client.auth.awaitInitialization()
    return client
}
