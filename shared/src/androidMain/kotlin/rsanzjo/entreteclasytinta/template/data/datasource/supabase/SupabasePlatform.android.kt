package rsanzjo.entreteclasytinta.template.data.datasource.supabase

import android.content.Intent
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.SupabaseClientBuilder
import io.github.jan.supabase.auth.handleDeeplinks
import io.github.jan.supabase.compose.auth.ComposeAuth
import io.github.jan.supabase.compose.auth.googleNativeLogin
import org.koin.mp.KoinPlatform
import rsanzjo.entreteclasytinta.template.config.AppConfig

internal actual fun SupabaseClientBuilder.installPlatformPlugins() {
    // Sin client ID no hay login nativo: el botón de Google usa el flujo OAuth por navegador.
    if (AppConfig.GOOGLE_WEB_CLIENT_ID.isNotBlank()) {
        install(ComposeAuth) {
            googleNativeLogin(serverClientId = AppConfig.GOOGLE_WEB_CLIENT_ID)
        }
    }
}

/**
 * Entrega a Supabase el deep link con el que se abrió la app (confirmación de correo, recuperación
 * de contraseña, login social). Llamar desde `onCreate` y `onNewIntent` de la Activity.
 */
fun handleAuthDeepLink(intent: Intent) {
    KoinPlatform.getKoin().get<SupabaseClient>().handleDeeplinks(intent)
}
