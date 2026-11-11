package rsanzjo.entreteclasytinta.template.data.datasource.supabase

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.SupabaseClientBuilder
import io.github.jan.supabase.auth.handleDeeplinks
import org.koin.mp.KoinPlatform
import platform.Foundation.NSURL

// En iOS el login social va por navegador (OAuth); no hay plugins propios de la plataforma.
internal actual fun SupabaseClientBuilder.installPlatformPlugins() = Unit

/**
 * Entrega a Supabase el deep link con el que se abrió la app (confirmación de correo, recuperación
 * de contraseña, login social). Llamar desde `.onOpenURL` en `iOSApp.swift`.
 */
fun handleAuthDeepLink(url: NSURL) {
    KoinPlatform.getKoin().get<SupabaseClient>().handleDeeplinks(url)
}
