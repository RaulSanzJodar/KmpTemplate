package rsanzjo.entreteclasytinta.template.presentation.feature.auth.login

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import rsanzjo.entreteclasytinta.template.domain.error.DomainError
import rsanzjo.entreteclasytinta.template.presentation.common.navigation.NavigationInclusive
import rsanzjo.entreteclasytinta.template.presentation.common.navigation.auth.AuthDestination
import rsanzjo.entreteclasytinta.template.presentation.common.navigation.main.MainDestination
import rsanzjo.entreteclasytinta.template.resources.Res
import rsanzjo.entreteclasytinta.template.resources.auth_invalid_credential
import rsanzjo.entreteclasytinta.template.testing.FakeAuthUseCase
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    private val toHome = NavigationInclusive(
        destination = MainDestination.HomeScreen,
        popUpTo = AuthDestination.LoginScreen
    )

    @BeforeTest
    fun setUp() = Dispatchers.setMain(UnconfinedTestDispatcher())

    @AfterTest
    fun tearDown() = Dispatchers.resetMain()

    private fun LoginViewModel.fill(email: String = "a@b.c", password: String = "secret") {
        manageAction(LoginAction.OnEmailChange(email))
        manageAction(LoginAction.OnPasswordChange(password))
    }

    @Test
    fun onStart_whenLoggedIn_navigatesToHome() = runTest {
        val vm = LoginViewModel(FakeAuthUseCase(loggedIn = true))
        vm.manageAction(LoginAction.OnStart)
        assertEquals(toHome, vm.navHostEvent.first())
    }

    @Test
    fun onStart_whenNotLoggedIn_showsForm() {
        val vm = LoginViewModel(FakeAuthUseCase(loggedIn = false))
        vm.manageAction(LoginAction.OnStart)
        assertTrue(vm.uiState.value.isFormVisible)
    }

    @Test
    fun login_success_callsUseCaseAndNavigates() = runTest {
        val auth = FakeAuthUseCase()
        val vm = LoginViewModel(auth)
        vm.fill()
        vm.manageAction(LoginAction.OnLoginClick)
        assertEquals(1, auth.loginCalls)
        assertEquals(toHome, vm.navHostEvent.first())
    }

    @Test
    fun login_withInvalidCredentials_sendsInvalidCredentialMessage() = runTest {
        val vm = LoginViewModel(FakeAuthUseCase(loginError = DomainError.Unauthorized()))
        vm.fill()
        vm.manageAction(LoginAction.OnLoginClick)
        assertEquals(LoginEffect.ShowMessage(Res.string.auth_invalid_credential), vm.effects.first())
    }

    @Test
    fun login_withBlankEmailOrPassword_doesNotCallUseCase() {
        val auth = FakeAuthUseCase()
        val vm = LoginViewModel(auth)

        vm.fill(email = "", password = "secret")
        vm.manageAction(LoginAction.OnLoginClick)
        vm.fill(email = "a@b.c", password = "")
        vm.manageAction(LoginAction.OnLoginClick)

        assertEquals(0, auth.loginCalls)
    }
}
