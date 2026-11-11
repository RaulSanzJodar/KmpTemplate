package rsanzjo.entreteclasytinta.template.presentation.feature.auth.register

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import rsanzjo.entreteclasytinta.template.presentation.common.auth.AuthFormScaffold
import rsanzjo.entreteclasytinta.template.presentation.common.auth.MIN_PASSWORD_LENGTH
import rsanzjo.entreteclasytinta.template.presentation.common.components.LoaderDialog
import rsanzjo.entreteclasytinta.template.presentation.common.navigation.NavHostEvent
import rsanzjo.entreteclasytinta.template.resources.*
import rsanzjo.entreteclasytinta.template.ui.theme.AppTheme

@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel = koinViewModel(),
    onNavEvent: (NavHostEvent) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val baseUiState by viewModel.baseUiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) { viewModel.navHostEvent.collect { event -> onNavEvent(event) } }

    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                is RegisterEffect.ShowMessage -> snackbarHostState.showSnackbar(getString(effect.messageRes))
            }
        }
    }

    if (baseUiState.isLoading) {
        LoaderDialog()
    }

    RegisterScreenContent(
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        onAction = viewModel::manageAction
    )
}

@Composable
fun RegisterScreenContent(
    uiState: RegisterUiState = RegisterUiState(),
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    onAction: (RegisterAction) -> Unit = {}
) {
    AuthFormScaffold(
        title = stringResource(Res.string.register_title),
        snackbarHostState = snackbarHostState
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = uiState.email,
            onValueChange = { onAction(RegisterAction.OnEmailChange(it)) },
            label = { Text(stringResource(Res.string.auth_email)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next)
        )
        Spacer(Modifier.height(12.dp))
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = uiState.password,
            onValueChange = { onAction(RegisterAction.OnPasswordChange(it)) },
            label = { Text(stringResource(Res.string.auth_password)) },
            supportingText = { Text(stringResource(Res.string.auth_password_min_format, MIN_PASSWORD_LENGTH)) },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Next)
        )
        Spacer(Modifier.height(12.dp))
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = uiState.confirmPassword,
            onValueChange = { onAction(RegisterAction.OnConfirmPasswordChange(it)) },
            label = { Text(stringResource(Res.string.auth_confirm_password)) },
            isError = uiState.passwordsMismatch,
            supportingText = {
                if (uiState.passwordsMismatch) Text(stringResource(Res.string.auth_passwords_mismatch))
            },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { onAction(RegisterAction.OnRegisterClick) })
        )
        Spacer(Modifier.height(24.dp))
        Button(
            enabled = uiState.isRegisterEnabled,
            onClick = { onAction(RegisterAction.OnRegisterClick) },
            modifier = Modifier.fillMaxWidth().height(48.dp)
        ) {
            Text(stringResource(Res.string.register_button))
        }
        TextButton(onClick = { onAction(RegisterAction.OnBackClick) }) {
            Text(stringResource(Res.string.register_have_account))
        }
    }
}

@PreviewLightDark
@Composable
private fun RegisterScreenPreview() {
    AppTheme {
        RegisterScreenContent(
            uiState = RegisterUiState(email = "usuario@ejemplo.com", password = "secreta", confirmPassword = "secret")
        )
    }
}
