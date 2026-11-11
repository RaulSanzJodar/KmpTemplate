package rsanzjo.entreteclasytinta.template.data.datasource.supabase

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.SupabaseClientBuilder
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.AuthConfig
import io.github.jan.supabase.auth.MemoryCodeVerifierCache
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.ktor.client.engine.HttpClientEngine
import rsanzjo.entreteclasytinta.template.common.platform.SecureStorage
import rsanzjo.entreteclasytinta.template.config.AppConfig

/**
 * Deep link al que vuelve la app tras confirmar el correo, recuperar la contraseña o el login
 * social: `<esquema>://auth`. Debe estar en Supabase > Authentication > URL Configuration >
 * Redirect URLs, en el `intent-filter` del manifest de Android y en `CFBundleURLTypes` de iOS.
 */
const val AUTH_DEEP_LINK_SCHEME = "rsanzjo.entreteclasytinta.template"
const val AUTH_DEEP_LINK_HOST = "auth"

/**
 * Cliente de Supabase de la app: Auth (sesión cifrada en [storage], refresco automático del token)
 * y Postgrest. [engine], [platformPlugins] y [configureAuth] existen para los tests.
 */
fun createAppSupabaseClient(
    storage: SecureStorage,
    url: String = AppConfig.SUPABASE_URL,
    key: String = AppConfig.SUPABASE_PUBLISHABLE_KEY,
    engine: HttpClientEngine? = null,
    platformPlugins: Boolean = true,
    configureAuth: AuthConfig.() -> Unit = {}
): SupabaseClient = createSupabaseClient(url, key) {
    httpEngine = engine

    install(Auth) {
        sessionManager = SecureSessionManager(storage)
        // Flujo implícito (el de por defecto): el enlace de recuperación trae `type=recovery`, que
        // es como se distingue de un inicio de sesión normal. Este caché solo lo usa PKCE; si se
        // cambia de flujo hay que persistirlo para sobrevivir a la muerte del proceso mientras el
        // usuario está en el navegador.
        codeVerifierCache = MemoryCodeVerifierCache()
        scheme = AUTH_DEEP_LINK_SCHEME
        host = AUTH_DEEP_LINK_HOST
        configureAuth()
    }
    install(Postgrest)
    // Storage, Realtime o Functions se añaden aquí con su `install(...)` y su dependencia del BOM.

    if (platformPlugins) installPlatformPlugins()
}

/** Plugins que solo existen en una plataforma (p. ej. el login nativo de Google en Android). */
internal expect fun SupabaseClientBuilder.installPlatformPlugins()
