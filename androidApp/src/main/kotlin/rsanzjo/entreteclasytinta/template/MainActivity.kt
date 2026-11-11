package rsanzjo.entreteclasytinta.template

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import rsanzjo.entreteclasytinta.template.data.datasource.supabase.handleAuthDeepLink

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // Solo en el primer arranque: al recrearse la Activity el intent es el mismo y ya se procesó.
        if (savedInstanceState == null) handleAuthDeepLink(intent)

        setContent(parent = null) {
            App()
        }
    }

    // Con `singleTask`, un enlace de autenticación llega aquí si la app ya estaba abierta.
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleAuthDeepLink(intent)
    }
}
