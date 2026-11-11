package rsanzjo.entreteclasytinta.template

import androidx.compose.ui.window.ComposeUIViewController
import platform.Foundation.NSURL
import platform.UIKit.UIViewController
import rsanzjo.entreteclasytinta.template.data.datasource.supabase.handleAuthDeepLink
import rsanzjo.entreteclasytinta.template.di.initKoin

/** Se llama una sola vez al arrancar la app iOS, antes de crear [MainViewController]. */
fun initKoinIos() = initKoin()

/** Se llama desde `.onOpenURL` cuando la app se abre con su esquema de URL (enlaces de autenticación). */
fun onOpenUrl(url: NSURL) = handleAuthDeepLink(url)

fun MainViewController(): UIViewController = ComposeUIViewController { App() }
