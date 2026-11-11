package rsanzjo.entreteclasytinta.template.presentation.feature.auth.forgotpassword

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import rsanzjo.entreteclasytinta.template.domain.error.DomainError
import rsanzjo.entreteclasytinta.template.resources.Res
import rsanzjo.entreteclasytinta.template.resources.auth_too_many_requests
import rsanzjo.entreteclasytinta.template.resources.forgot_password_sent
import rsanzjo.entreteclasytinta.template.testing.FakeAuthUseCase
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class ForgotPasswordViewModelTest {

    @BeforeTest
    fun setUp() = Dispatchers.setMain(UnconfinedTestDispatcher())

    @AfterTest
    fun tearDown() = Dispatchers.resetMain()

    @Test
    fun send_requestsTheResetAndConfirms() = runTest {
        val auth = FakeAuthUseCase()
        val vm = ForgotPasswordViewModel(auth)
        vm.manageAction(ForgotPasswordAction.OnEmailChange("ada@example.com"))
        vm.manageAction(ForgotPasswordAction.OnSendClick)
        assertEquals(listOf("ada@example.com"), auth.passwordResets)
        assertEquals(ForgotPasswordEffect.ShowMessage(Res.string.forgot_password_sent), vm.effects.first())
    }

    @Test
    fun send_whenRateLimited_showsItsMessage() = runTest {
        val vm = ForgotPasswordViewModel(FakeAuthUseCase(error = DomainError.TooManyRequests()))
        vm.manageAction(ForgotPasswordAction.OnEmailChange("ada@example.com"))
        vm.manageAction(ForgotPasswordAction.OnSendClick)
        assertEquals(ForgotPasswordEffect.ShowMessage(Res.string.auth_too_many_requests), vm.effects.first())
    }

    @Test
    fun send_withInvalidEmail_doesNothing() {
        val auth = FakeAuthUseCase()
        val vm = ForgotPasswordViewModel(auth)
        vm.manageAction(ForgotPasswordAction.OnEmailChange("not-an-email"))
        vm.manageAction(ForgotPasswordAction.OnSendClick)
        assertTrue(auth.passwordResets.isEmpty())
    }
}
