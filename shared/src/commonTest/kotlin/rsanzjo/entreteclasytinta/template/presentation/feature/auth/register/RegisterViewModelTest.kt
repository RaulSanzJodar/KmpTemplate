package rsanzjo.entreteclasytinta.template.presentation.feature.auth.register

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import rsanzjo.entreteclasytinta.template.domain.error.DomainError
import rsanzjo.entreteclasytinta.template.presentation.common.navigation.BackNavigation
import rsanzjo.entreteclasytinta.template.resources.Res
import rsanzjo.entreteclasytinta.template.resources.auth_user_already_exists
import rsanzjo.entreteclasytinta.template.resources.register_confirmation_sent
import rsanzjo.entreteclasytinta.template.testing.FakeAuthUseCase
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class RegisterViewModelTest {

    @BeforeTest
    fun setUp() = Dispatchers.setMain(UnconfinedTestDispatcher())

    @AfterTest
    fun tearDown() = Dispatchers.resetMain()

    private fun RegisterViewModel.fill(
        email: String = "ada@example.com",
        password: String = "secret1",
        confirm: String = password
    ) {
        manageAction(RegisterAction.OnEmailChange(email))
        manageAction(RegisterAction.OnPasswordChange(password))
        manageAction(RegisterAction.OnConfirmPasswordChange(confirm))
    }

    @Test
    fun register_callsUseCaseWithTheEmail() {
        val auth = FakeAuthUseCase()
        val vm = RegisterViewModel(auth)
        vm.fill()
        vm.manageAction(RegisterAction.OnRegisterClick)
        assertEquals(listOf("ada@example.com"), auth.registeredEmails)
    }

    @Test
    fun register_whenEmailMustBeConfirmed_showsMessageAndClearsTheForm() = runTest {
        val vm = RegisterViewModel(FakeAuthUseCase(registerSignsIn = false))
        vm.fill()
        vm.manageAction(RegisterAction.OnRegisterClick)
        assertEquals(RegisterEffect.ShowMessage(Res.string.register_confirmation_sent), vm.effects.first())
        assertEquals(RegisterUiState(), vm.uiState.value)
    }

    @Test
    fun register_withExistingEmail_showsItsMessage() = runTest {
        val vm = RegisterViewModel(FakeAuthUseCase(error = DomainError.UserAlreadyExists()))
        vm.fill()
        vm.manageAction(RegisterAction.OnRegisterClick)
        assertEquals(RegisterEffect.ShowMessage(Res.string.auth_user_already_exists), vm.effects.first())
    }

    @Test
    fun register_isDisabledUntilTheFormIsValid() {
        val auth = FakeAuthUseCase()
        val vm = RegisterViewModel(auth)

        vm.fill(email = "not-an-email")
        assertFalse(vm.uiState.value.isRegisterEnabled)
        vm.fill(password = "short", confirm = "short")
        assertFalse(vm.uiState.value.isRegisterEnabled)
        vm.fill(confirm = "different")
        assertFalse(vm.uiState.value.isRegisterEnabled)
        assertTrue(vm.uiState.value.passwordsMismatch)

        vm.manageAction(RegisterAction.OnRegisterClick)
        assertTrue(auth.registeredEmails.isEmpty())
    }

    @Test
    fun backClick_navigatesBack() = runTest {
        val vm = RegisterViewModel(FakeAuthUseCase())
        vm.manageAction(RegisterAction.OnBackClick)
        assertEquals(BackNavigation(), vm.navHostEvent.first())
    }
}
