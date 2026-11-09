package rsanzjo.entreteclasytinta.template.presentation.feature.main.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import rsanzjo.entreteclasytinta.template.domain.entity.UserProfile
import rsanzjo.entreteclasytinta.template.presentation.common.base.BaseUiState
import rsanzjo.entreteclasytinta.template.presentation.common.base.errorText
import rsanzjo.entreteclasytinta.template.presentation.common.components.DefaultCard
import rsanzjo.entreteclasytinta.template.presentation.common.components.LoaderDialog
import rsanzjo.entreteclasytinta.template.presentation.common.components.SectionTitle
import rsanzjo.entreteclasytinta.template.presentation.common.navigation.NavHostEvent
import rsanzjo.entreteclasytinta.template.resources.*
import rsanzjo.entreteclasytinta.template.ui.theme.AppTheme

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = koinViewModel(),
    onNavEvent: (NavHostEvent) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val baseUiState by viewModel.baseUiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) { viewModel.navHostEvent.collect { event -> onNavEvent(event) } }
    LaunchedEffect(Unit) { viewModel.manageAction(HomeAction.OnStart) }

    if (baseUiState.isLoading) {
        LoaderDialog()
    }

    HomeScreenContent(
        profile = uiState.profile,
        baseUiState = baseUiState,
        onAction = viewModel::manageAction
    )
}

@Composable
fun HomeScreenContent(
    profile: UserProfile?,
    baseUiState: BaseUiState = BaseUiState(),
    onAction: (HomeAction) -> Unit = {}
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SectionTitle(text = stringResource(Res.string.home_title))

            profile?.let {
                DefaultCard {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = stringResource(Res.string.home_welcome_format, it.name),
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(text = it.email, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }

            baseUiState.errorText()?.let {
                Text(text = it, color = MaterialTheme.colorScheme.error)
            }

            Button(onClick = { onAction(HomeAction.OnLogoutClick) }) {
                Text(stringResource(Res.string.home_logout))
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun HomeScreenPreview() {
    AppTheme {
        HomeScreenContent(profile = UserProfile(id = "1", name = "Ada", email = "ada@example.com"))
    }
}
