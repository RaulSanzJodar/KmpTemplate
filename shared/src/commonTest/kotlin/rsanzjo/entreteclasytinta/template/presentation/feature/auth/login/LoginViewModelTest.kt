package rsanzjo.entreteclasytinta.template.presentation.feature.auth.login

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import rsanzjo.entreteclasytinta.template.domain.entity.SocialProvider
import rsanzjo.entreteclasytinta.template.domain.error.DomainError
import rsanzjo.entreteclasytinta.template.presentation.common.navigation.Navigation
import rsanzjo.entreteclasytinta.template.presentation.common.navigation.auth.AuthDestination
import rsanzjo.entreteclasytinta.template.resources.Res
import rsanzjo.entreteclasytinta.template.resources.auth_invalid_credential
import rsanzjo.entreteclasytinta.template.resources.error_unknown
import rsanzjo.entreteclasytinta.template.testing.FakeAuthUseCase
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    @BeforeTest
    fun setUp() = Dispatchers.setMain(UnconfinedTestDispatcher())

    @AfterTest
    fun tearDown() = Dispatchers.resetMain()

    private fun LoginViewModel.fill(email: String = "a@b.c", password: String = "secret") {
        manageAction(LoginAction.OnEmailChange(email))
        manageAction(LoginAction.OnPasswordChange(password))
    }

    @Test
    fun login_callsUseCase() {
        val auth = FakeAuthUseCase()
        val vm = LoginViewModel(auth)
        vm.fill()
        vm.manageAction(LoginAction.OnLoginClick)
        assertEquals(1, auth.loginCalls)
    }

    @Test
    fun login_withInvalidCredentials_sendsInvalidCredentialMessage() = runTest {
        val vm = LoginViewModel(FakeAuthUseCase(error = DomainError.InvalidCredentials()))
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

    @Test
    fun socialClick_startsLoginWithThatProvider() {
        val auth = FakeAuthUseCase()
        val vm = LoginViewModel(auth)
        vm.manageAction(LoginAction.OnSocialClick(SocialProvider.Apple))
        assertEquals(listOf(SocialProvider.Apple), auth.socialLogins)
    }

    @Test
    fun nativeSignInError_sendsGenericMessage() = runTest {
        val vm = LoginViewModel(FakeAuthUseCase())
        vm.manageAction(LoginAction.OnNativeSignInError)
        assertEquals(LoginEffect.ShowMessage(Res.string.error_unknown), vm.effects.first())
    }

    @Test
    fun registerAndForgotPasswordClicks_navigateToTheirScreens() = runTest {
        val vm = LoginViewModel(FakeAuthUseCase())
        vm.manageAction(LoginAction.OnRegisterClick)
        assertEquals(Navigation(AuthDestination.RegisterScreen), vm.navHostEvent.first())
        vm.manageAction(LoginAction.OnForgotPasswordClick)
        assertEquals(Navigation(AuthDestination.ForgotPasswordScreen), vm.navHostEvent.first())
    }
}
