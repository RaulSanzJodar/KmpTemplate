package rsanzjo.entreteclasytinta.template

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import coil3.ImageLoader
import coil3.compose.setSingletonImageLoaderFactory
import coil3.network.ktor3.KtorNetworkFetcherFactory
import rsanzjo.entreteclasytinta.template.presentation.common.navigation.auth.AuthDestination
import rsanzjo.entreteclasytinta.template.presentation.common.navigation.auth.authGraph
import rsanzjo.entreteclasytinta.template.presentation.common.navigation.main.mainGraph
import rsanzjo.entreteclasytinta.template.ui.theme.AppTheme

/** Raíz de la UI compartida por Android e iOS. Koin debe estar iniciado antes de componerla. */
@Composable
fun App() {
    setSingletonImageLoaderFactory { context ->
        ImageLoader.Builder(context)
            .components { add(KtorNetworkFetcherFactory()) }
            .build()
    }

    AppTheme {
        val navController = rememberNavController()

        NavHost(
            navController = navController,
            startDestination = AuthDestination.LoginScreen,
            modifier = Modifier.fillMaxSize()
        ) {
            authGraph(navController)
            mainGraph(navController)
        }
    }
}
