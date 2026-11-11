package rsanzjo.entreteclasytinta.template.presentation.common.auth

import androidx.compose.runtime.Composable

/**
 * Lanzador del inicio de sesión nativo de Google (selector de cuentas del sistema), o `null` si
 * la plataforma no lo ofrece o no está configurado; en ese caso se usa el flujo por navegador de
 * `AuthUseCase.loginWith`. Si el usuario lo completa, la sesión llega por `AuthUseCase.sessionState`.
 *
 * Es el único punto de presentation que toca el SDK de Supabase (ver sus `actual`): el selector
 * nativo necesita la Activity y solo existe como composable.
 */
@Composable
expect fun rememberNativeGoogleSignIn(onError: () -> Unit): (() -> Unit)?
