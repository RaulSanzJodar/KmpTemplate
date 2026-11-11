package rsanzjo.entreteclasytinta.template.data.datasource.supabase

import io.github.jan.supabase.auth.status.SessionSource
import io.github.jan.supabase.auth.status.SessionStatus
import io.github.jan.supabase.auth.user.UserSession
import rsanzjo.entreteclasytinta.template.domain.entity.SessionState
import kotlin.test.Test
import kotlin.test.assertEquals

class SessionStateMapperTest {

    private fun session(type: String = "") =
        UserSession(accessToken = "a", refreshToken = "r", expiresIn = 3600, tokenType = "bearer", type = type)

    private fun SessionStatus.map(previous: SessionState = SessionState.Loading, signOutRequested: Boolean = false) =
        toSessionState(previous, signOutRequested)

    @Test
    fun initializing_atStartup_isLoading() {
        assertEquals(SessionState.Loading, SessionStatus.Initializing.map())
    }

    @Test
    fun initializing_afterReturningFromBackground_keepsThePreviousState() {
        assertEquals(
            SessionState.Authenticated,
            SessionStatus.Initializing.map(previous = SessionState.Authenticated)
        )
    }

    @Test
    fun authenticated_isAuthenticated() {
        assertEquals(SessionState.Authenticated, SessionStatus.Authenticated(session(), SessionSource.Storage).map())
    }

    @Test
    fun authenticatedFromRecoveryLink_isPasswordRecovery() {
        val status = SessionStatus.Authenticated(session(type = "recovery"), SessionSource.External)
        assertEquals(SessionState.PasswordRecovery, status.map())
    }

    @Test
    fun recoverySessionAfterChangingThePassword_isAuthenticated() {
        val recovery = session(type = "recovery")
        val status = SessionStatus.Authenticated(recovery, SessionSource.UserChanged(recovery))
        assertEquals(SessionState.Authenticated, status.map(previous = SessionState.PasswordRecovery))
    }

    @Test
    fun notAuthenticated_atStartup_isNotExpired() {
        assertEquals(SessionState.Unauthenticated(expired = false), SessionStatus.NotAuthenticated().map())
    }

    @Test
    fun notAuthenticated_afterUserSignOut_isNotExpired() {
        val state = SessionStatus.NotAuthenticated(isSignOut = true)
            .map(previous = SessionState.Authenticated, signOutRequested = true)
        assertEquals(SessionState.Unauthenticated(expired = false), state)
    }

    @Test
    fun notAuthenticated_whenSessionIsRevoked_isExpired() {
        // Supabase también marca `isSignOut` cuando descarta la sesión por un refresh token inválido.
        val state = SessionStatus.NotAuthenticated(isSignOut = true).map(previous = SessionState.Authenticated)
        assertEquals(SessionState.Unauthenticated(expired = true), state)
    }
}
