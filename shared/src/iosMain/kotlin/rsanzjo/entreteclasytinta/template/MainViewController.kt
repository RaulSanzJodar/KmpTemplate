package rsanzjo.entreteclasytinta.template

import androidx.compose.ui.window.ComposeUIViewController
import platform.UIKit.UIViewController

/** Se llama una sola vez al arrancar la app iOS, antes de crear [MainViewController]. */
fun initKoinIos() = Unit

fun MainViewController(): UIViewController = ComposeUIViewController { App() }
