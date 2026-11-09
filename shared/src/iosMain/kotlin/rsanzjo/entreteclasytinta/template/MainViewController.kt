package rsanzjo.entreteclasytinta.template

import androidx.compose.ui.window.ComposeUIViewController
import platform.UIKit.UIViewController
import rsanzjo.entreteclasytinta.template.di.initKoin

/** Se llama una sola vez al arrancar la app iOS, antes de crear [MainViewController]. */
fun initKoinIos() = initKoin()

fun MainViewController(): UIViewController = ComposeUIViewController { App() }
