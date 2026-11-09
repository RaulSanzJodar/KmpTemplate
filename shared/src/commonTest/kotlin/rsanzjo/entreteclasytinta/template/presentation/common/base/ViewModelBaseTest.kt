package rsanzjo.entreteclasytinta.template.presentation.common.base

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import rsanzjo.entreteclasytinta.template.domain.error.DomainError
import rsanzjo.entreteclasytinta.template.resources.Res
import rsanzjo.entreteclasytinta.template.resources.network_exception
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class ViewModelBaseTest {

    private class TestViewModel : ViewModelBase<Int, Unit, Nothing>(0) {
        override fun manageAction(action: Unit) = Unit
        fun run(block: suspend () -> Unit) = launchWithLoading { block() }
    }

    @BeforeTest
    fun setUp() = Dispatchers.setMain(UnconfinedTestDispatcher())

    @AfterTest
    fun tearDown() = Dispatchers.resetMain()

    @Test
    fun launchWithLoading_setsLoadingUntilBlockFinishes() {
        val vm = TestViewModel()
        val gate = CompletableDeferred<Unit>()

        vm.run { gate.await() }
        assertTrue(vm.baseUiState.value.isLoading)

        gate.complete(Unit)
        assertFalse(vm.baseUiState.value.isLoading)
    }

    @Test
    fun overlappingLoads_keepLoadingUntilLastFinishes() {
        val vm = TestViewModel()
        val first = CompletableDeferred<Unit>()
        val second = CompletableDeferred<Unit>()

        vm.run { first.await() }
        vm.run { second.await() }

        first.complete(Unit)
        assertTrue(vm.baseUiState.value.isLoading)

        second.complete(Unit)
        assertFalse(vm.baseUiState.value.isLoading)
    }

    @Test
    fun failingBlock_publishesErrorState() {
        val vm = TestViewModel()

        vm.run { throw DomainError.Network() }

        assertEquals(Res.string.network_exception, vm.baseUiState.value.errorMessageRes)
    }
}
