package rsanzjo.entreteclasytinta.template.presentation

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import rsanzjo.entreteclasytinta.template.domain.entity.SessionState
import rsanzjo.entreteclasytinta.template.testing.FakeAuthUseCase
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class AppViewModelTest {

    @BeforeTest
    fun setUp() = Dispatchers.setMain(UnconfinedTestDispatcher())

    @AfterTest
    fun tearDown() = Dispatchers.resetMain()

    @Test
    fun sessionState_followsTheSession() {
        val auth = FakeAuthUseCase()
        val vm = AppViewModel(auth)
        assertEquals(SessionState.Unauthenticated(), vm.sessionState.value)

        auth.sessionState.value = SessionState.Authenticated
        assertEquals(SessionState.Authenticated, vm.sessionState.value)

        auth.sessionState.value = SessionState.Unauthenticated(expired = true)
        assertEquals(SessionState.Unauthenticated(expired = true), vm.sessionState.value)
    }
}
