package rsanzjo.entreteclasytinta.template.presentation.feature.auth.newpassword

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import rsanzjo.entreteclasytinta.template.domain.error.DomainError
import rsanzjo.entreteclasytinta.template.resources.Res
import rsanzjo.entreteclasytinta.template.resources.auth_weak_password
import rsanzjo.entreteclasytinta.template.testing.FakeAuthUseCase
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class NewPasswordViewModelTest {

    @BeforeTest
    fun setUp() = Dispatchers.setMain(UnconfinedTestDispatcher())

    @AfterTest
    fun tearDown() = Dispatchers.resetMain()

    private fun NewPasswordViewModel.fill(password: String = "secret1", confirm: String = password) {
        manageAction(NewPasswordAction.OnPasswordChange(password))
        manageAction(NewPasswordAction.OnConfirmPasswordChange(confirm))
    }

    @Test
    fun save_updatesThePassword() {
        val auth = FakeAuthUseCase()
        val vm = NewPasswordViewModel(auth)
        vm.fill()
        vm.manageAction(NewPasswordAction.OnSaveClick)
        assertEquals(listOf("secret1"), auth.updatedPasswords)
    }

    @Test
    fun save_withWeakPassword_showsItsMessage() = runTest {
        val vm = NewPasswordViewModel(FakeAuthUseCase(error = DomainError.WeakPassword()))
        vm.fill()
        vm.manageAction(NewPasswordAction.OnSaveClick)
        assertEquals(NewPasswordEffect.ShowMessage(Res.string.auth_weak_password), vm.effects.first())
    }

    @Test
    fun save_withMismatchedOrShortPassword_doesNothing() {
        val auth = FakeAuthUseCase()
        val vm = NewPasswordViewModel(auth)

        vm.fill(confirm = "different")
        vm.manageAction(NewPasswordAction.OnSaveClick)
        vm.fill(password = "short")
        vm.manageAction(NewPasswordAction.OnSaveClick)

        assertTrue(auth.updatedPasswords.isEmpty())
    }
}
