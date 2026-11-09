package rsanzjo.entreteclasytinta.template.presentation.feature.main.home

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import rsanzjo.entreteclasytinta.template.domain.entity.UserProfile
import rsanzjo.entreteclasytinta.template.domain.error.DomainError
import rsanzjo.entreteclasytinta.template.presentation.common.navigation.NavigationInclusive
import rsanzjo.entreteclasytinta.template.presentation.common.navigation.auth.AuthDestination
import rsanzjo.entreteclasytinta.template.presentation.common.navigation.main.MainDestination
import rsanzjo.entreteclasytinta.template.resources.Res
import rsanzjo.entreteclasytinta.template.resources.network_exception
import rsanzjo.entreteclasytinta.template.testing.FakeAuthUseCase
import rsanzjo.entreteclasytinta.template.testing.FakeProfileUseCase
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    @BeforeTest
    fun setUp() = Dispatchers.setMain(UnconfinedTestDispatcher())

    @AfterTest
    fun tearDown() = Dispatchers.resetMain()

    @Test
    fun onStart_loadsProfile() {
        val profile = UserProfile(id = "7", name = "Grace", email = "grace@example.com")
        val vm = HomeViewModel(FakeProfileUseCase(profile = profile), FakeAuthUseCase())
        vm.manageAction(HomeAction.OnStart)
        assertEquals(profile, vm.uiState.value.profile)
    }

    @Test
    fun onStart_failure_publishesErrorInBaseUiState() {
        val vm = HomeViewModel(FakeProfileUseCase(error = DomainError.Network()), FakeAuthUseCase())
        vm.manageAction(HomeAction.OnStart)
        assertEquals(Res.string.network_exception, vm.baseUiState.value.errorMessageRes)
    }

    @Test
    fun logout_callsUseCaseAndNavigatesToLogin() = runTest {
        val auth = FakeAuthUseCase()
        val vm = HomeViewModel(FakeProfileUseCase(), auth)
        vm.manageAction(HomeAction.OnLogoutClick)
        assertEquals(1, auth.logoutCalls)
        assertEquals(
            NavigationInclusive(AuthDestination.LoginScreen, popUpTo = MainDestination.HomeScreen),
            vm.navHostEvent.first()
        )
    }
}
