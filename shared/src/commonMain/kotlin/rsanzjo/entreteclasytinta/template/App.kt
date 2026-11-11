package rsanzjo.entreteclasytinta.template

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import coil3.ImageLoader
import coil3.compose.setSingletonImageLoaderFactory
import coil3.network.ktor3.KtorNetworkFetcherFactory
import org.jetbrains.compose.resources.getString
import org.koin.compose.viewmodel.koinViewModel
import rsanzjo.entreteclasytinta.template.domain.entity.SessionState
import rsanzjo.entreteclasytinta.template.presentation.AppViewModel
import rsanzjo.entreteclasytinta.template.presentation.common.navigation.AppDestination
import rsanzjo.entreteclasytinta.template.presentation.common.navigation.auth.AuthDestination
import rsanzjo.entreteclasytinta.template.presentation.common.navigation.auth.authGraph
import rsanzjo.entreteclasytinta.template.presentation.common.navigation.main.MainDestination
import rsanzjo.entreteclasytinta.template.presentation.common.navigation.main.mainGraph
import rsanzjo.entreteclasytinta.template.resources.Res
import rsanzjo.entreteclasytinta.template.resources.error_session_expired
import rsanzjo.entreteclasytinta.template.ui.theme.AppTheme

/** Raíz de la UI compartida por Android e iOS. Koin debe estar iniciado antes de componerla. */
@Composable
fun App(viewModel: AppViewModel = koinViewModel()) {
    setSingletonImageLoaderFactory { context ->
        ImageLoader.Builder(context)
            .components { add(KtorNetworkFetcherFactory()) }
            .build()
    }

    AppTheme {
        val sessionState by viewModel.sessionState.collectAsStateWithLifecycle()

        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            when (val state = sessionState) {
                // Solo ocurre al arrancar, mientras se lee la sesión guardada.
                SessionState.Loading -> Box(contentAlignment = Alignment.Center) { CircularProgressIndicator() }
                else -> AppNavHost(sessionState = state)
            }
        }
    }
}

/**
 * La zona visible la decide [sessionState]: al cambiar (inicio o cierre de sesión, sesión caducada,
 * enlace de recuperación) se navega a su destino raíz vaciando la pila.
 */
@Composable
private fun AppNavHost(sessionState: SessionState) {
    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }
    // El destino inicial ya corresponde al primer estado; solo se navega con los siguientes.
    val startDestination = remember { sessionState.rootDestination() }
    var shownDestination by remember { mutableStateOf(startDestination) }

    LaunchedEffect(sessionState) {
        val destination = sessionState.rootDestination()
        if (destination != shownDestination) {
            shownDestination = destination
            navController.navigate(destination) {
                popUpTo(navController.graph.id) { inclusive = true }
                launchSingleTop = true
            }
        }
        if (sessionState is SessionState.Unauthenticated && sessionState.expired) {
            snackbarHostState.showSnackbar(getString(Res.string.error_session_expired))
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.fillMaxSize()
        ) {
            authGraph(navController)
            mainGraph(navController)
        }
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter).navigationBarsPadding()
        )
    }
}

private fun SessionState.rootDestination(): AppDestination = when (this) {
    SessionState.Authenticated -> MainDestination.HomeScreen
    SessionState.PasswordRecovery -> AuthDestination.NewPasswordScreen
    SessionState.Loading, is SessionState.Unauthenticated -> AuthDestination.LoginScreen
}
