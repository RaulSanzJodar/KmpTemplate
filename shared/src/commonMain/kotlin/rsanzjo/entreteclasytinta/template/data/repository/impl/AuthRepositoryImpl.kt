package rsanzjo.entreteclasytinta.template.data.repository.impl

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.Apple
import io.github.jan.supabase.auth.providers.Google
import io.github.jan.supabase.auth.providers.builtin.Email
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.scan
import rsanzjo.entreteclasytinta.template.data.datasource.supabase.toSessionState
import rsanzjo.entreteclasytinta.template.data.errorHandler.domainCall
import rsanzjo.entreteclasytinta.template.domain.entity.SessionState
import rsanzjo.entreteclasytinta.template.domain.entity.SocialProvider
import rsanzjo.entreteclasytinta.template.domain.error.DomainError
import rsanzjo.entreteclasytinta.template.domain.repository.AuthRepository
import kotlin.coroutines.cancellation.CancellationException

class AuthRepositoryImpl(supabase: SupabaseClient) : AuthRepository {

    private val auth = supabase.auth

    // Supabase no distingue un cierre de sesión voluntario de una sesión revocada; se anota aquí.
    private val signOutRequested = MutableStateFlow(false)

    override val sessionState: Flow<SessionState> = auth.sessionStatus
        .scan<_, SessionState>(SessionState.Loading) { previous, status ->
            status.toSessionState(previous, signOutRequested.value).also {
                if (it is SessionState.Unauthenticated) signOutRequested.value = false
            }
        }
        .distinctUntilChanged()

    override suspend fun signIn(email: String, password: String) = domainCall {
        auth.signInWith(Email) {
            this.email = email
            this.password = password
        }
    }

    override suspend fun signInWith(provider: SocialProvider) = domainCall {
        auth.signInWith(
            when (provider) {
                SocialProvider.Google -> Google
                SocialProvider.Apple -> Apple
            }
        )
    }

    override suspend fun signUp(email: String, password: String): Boolean = domainCall {
        val user = auth.signUpWith(Email) {
            this.email = email
            this.password = password
        }
        // Con la confirmación por correo activa, Supabase no revela que el correo ya existe:
        // responde con un usuario sin identidades en lugar de un error.
        if (user?.identities?.isEmpty() == true) throw DomainError.UserAlreadyExists()
        auth.currentSessionOrNull() != null
    }

    override suspend fun signOut() {
        signOutRequested.value = true
        try {
            auth.signOut()
        } catch (e: CancellationException) {
            throw e
        } catch (_: Exception) {
            // Sin red o con error del servidor, la sesión se cierra igualmente en el dispositivo.
            auth.clearSession()
        }
    }

    override suspend fun sendPasswordReset(email: String) = domainCall {
        auth.resetPasswordForEmail(email)
    }

    override suspend fun updatePassword(newPassword: String) {
        domainCall {
            auth.updateUser { password = newPassword }
        }
    }
}
