package rsanzjo.entreteclasytinta.template.presentation.common.auth

import androidx.compose.runtime.Composable

// El login nativo de Google en iOS exige añadir el SDK GoogleSignIn al proyecto Xcode y el módulo
// `compose-auth` a iosMain; mientras tanto se usa el flujo por navegador.
@Composable
actual fun rememberNativeGoogleSignIn(onError: () -> Unit): (() -> Unit)? = null
