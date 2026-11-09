package rsanzjo.entreteclasytinta.template.data.datasource

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import rsanzjo.entreteclasytinta.template.testing.FakeSecureStorage
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class SessionManagerTest {

    @Test
    fun saveTokens_exposesBoth() = runTest {
        val manager = SessionManager(FakeSecureStorage())
        manager.saveTokens("a", "b")
        assertEquals("a", manager.token.first())
        assertEquals("b", manager.refreshToken.first())
    }

    @Test
    fun saveTokens_withoutRefresh_clearsPreviousRefresh() = runTest {
        val manager = SessionManager(FakeSecureStorage())
        manager.saveTokens("a", "b")
        manager.saveTokens("c", null)
        assertEquals("c", manager.token.first())
        assertNull(manager.refreshToken.first())
    }

    @Test
    fun clearToken_clearsBoth() = runTest {
        val manager = SessionManager(FakeSecureStorage())
        manager.saveTokens("a", "b")
        manager.clearToken()
        assertNull(manager.token.first())
        assertNull(manager.refreshToken.first())
    }

    @Test
    fun newManager_recoversTokensFromStorage() = runTest {
        val storage = FakeSecureStorage()
        SessionManager(storage).saveTokens("a", "b")
        val recovered = SessionManager(storage)
        assertEquals("a", recovered.token.first())
        assertEquals("b", recovered.refreshToken.first())
    }
}
