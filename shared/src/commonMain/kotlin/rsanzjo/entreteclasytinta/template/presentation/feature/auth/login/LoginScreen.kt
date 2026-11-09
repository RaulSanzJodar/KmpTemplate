package rsanzjo.entreteclasytinta.template.presentation.feature.auth.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import rsanzjo.entreteclasytinta.template.presentation.common.components.LoaderDialog
import rsanzjo.entreteclasytinta.template.presentation.common.navigation.NavHostEvent
import rsanzjo.entreteclasytinta.template.resources.*
import rsanzjo.entreteclasytinta.template.ui.theme.AppTheme

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = koinViewModel(),
    onNavEvent: (NavHostEvent) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val baseUiState by viewModel.baseUiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) { viewModel.navHostEvent.collect { event -> onNavEvent(event) } }

    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                is LoginEffect.ShowMessage -> snackbarHostState.showSnackbar(getString(effect.messageRes))
            }
        }
    }

    LaunchedEffect(Unit) { viewModel.manageAction(LoginAction.OnStart) }

    if (baseUiState.isLoading) {
        LoaderDialog()
    }

    if (uiState.isFormVisible) {
        LoginScreenContent(
            email = uiState.email,
            password = uiState.password,
            isLoginEnabled = uiState.isLoginEnabled,
            snackbarHostState = snackbarHostState,
            onAction = viewModel::manageAction
        )
    }
}

@Composable
fun LoginScreenContent(
    email: String = "",
    password: String = "",
    isLoginEnabled: Boolean = false,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    onAction: (LoginAction) -> Unit = {}
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding)
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(Res.string.login_title),
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(Modifier.height(32.dp))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = email,
                onValueChange = { onAction(LoginAction.OnEmailChange(it)) },
                label = { Text(stringResource(Res.string.login_email)) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next)
            )
            Spacer(Modifier.height(12.dp))
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = password,
                onValueChange = { onAction(LoginAction.OnPasswordChange(it)) },
                label = { Text(stringResource(Res.string.login_password)) },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { onAction(LoginAction.OnLoginClick) })
            )
            Spacer(Modifier.height(24.dp))
            Button(
                enabled = isLoginEnabled,
                onClick = { onAction(LoginAction.OnLoginClick) },
                modifier = Modifier.fillMaxWidth().height(48.dp)
            ) {
                Text(stringResource(Res.string.login_button))
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun LoginScreenPreview() {
    AppTheme { LoginScreenContent(email = "usuario@ejemplo.com", isLoginEnabled = true) }
}
