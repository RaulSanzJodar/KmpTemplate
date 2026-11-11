package rsanzjo.entreteclasytinta.template.presentation.common.auth

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.compose.auth.ComposeAuth
import io.github.jan.supabase.compose.auth.composable.NativeSignInResult
import io.github.jan.supabase.compose.auth.composable.rememberSignInWithGoogle
import org.koin.compose.koinInject

@Composable
actual fun rememberNativeGoogleSignIn(onError: () -> Unit): (() -> Unit)? {
    val supabase = koinInject<SupabaseClient>()
    // El plugin solo se instala si hay GOOGLE_WEB_CLIENT_ID (ver `installPlatformPlugins`).
    val composeAuth = remember(supabase) { supabase.pluginManager.getPluginOrNull(ComposeAuth) } ?: return null

    val signInState = composeAuth.rememberSignInWithGoogle(
        onResult = { result ->
            when (result) {
                is NativeSignInResult.Error, is NativeSignInResult.NetworkError -> onError()
                // El éxito se refleja en la sesión; cerrar el selector no es un error.
                is NativeSignInResult.Success, NativeSignInResult.ClosedByUser -> Unit
            }
        }
    )
    return remember(signInState) { { signInState.startFlow() } }
}
