package rsanzjo.entreteclasytinta.template.data.datasource.supabase

import io.github.jan.supabase.auth.user.UserSession
import kotlinx.coroutines.test.runTest
import rsanzjo.entreteclasytinta.template.testing.FakeSecureStorage
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class SecureSessionManagerTest {

    private val storage = FakeSecureStorage()
    private val manager = SecureSessionManager(storage)
    private val session = UserSession(accessToken = "a", refreshToken = "r", expiresIn = 3600, tokenType = "bearer")

    @Test
    fun savedSession_isRestoredWithTheSameExpiry() = runTest {
        manager.saveSession(session)
        // Otro gestor sobre el mismo almacén, como tras reiniciar la app.
        assertEquals(session, SecureSessionManager(storage).loadSessionOrNull())
    }

    @Test
    fun withoutSavedSession_loadsNull() = runTest {
        assertNull(manager.loadSessionOrNull())
    }

    @Test
    fun deleteSession_removesIt() = runTest {
        manager.saveSession(session)
        manager.deleteSession()
        assertNull(manager.loadSessionOrNull())
    }

    @Test
    fun undecodableSession_isDiscarded() = runTest {
        storage.putString("supabase_session", "not json")
        assertNull(manager.loadSessionOrNull())
        assertNull(storage.getString("supabase_session"))
    }
}
