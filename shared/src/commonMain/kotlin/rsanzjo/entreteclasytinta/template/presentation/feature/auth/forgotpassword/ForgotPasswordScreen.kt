package rsanzjo.entreteclasytinta.template.presentation.feature.auth.forgotpassword

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import rsanzjo.entreteclasytinta.template.presentation.common.auth.AuthFormScaffold
import rsanzjo.entreteclasytinta.template.presentation.common.components.LoaderDialog
import rsanzjo.entreteclasytinta.template.presentation.common.navigation.NavHostEvent
import rsanzjo.entreteclasytinta.template.resources.*
import rsanzjo.entreteclasytinta.template.ui.theme.AppTheme

@Composable
fun ForgotPasswordScreen(
    viewModel: ForgotPasswordViewModel = koinViewModel(),
    onNavEvent: (NavHostEvent) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val baseUiState by viewModel.baseUiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) { viewModel.navHostEvent.collect { event -> onNavEvent(event) } }

    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                is ForgotPasswordEffect.ShowMessage -> snackbarHostState.showSnackbar(getString(effect.messageRes))
            }
        }
    }

    if (baseUiState.isLoading) {
        LoaderDialog()
    }

    ForgotPasswordScreenContent(
        email = uiState.email,
        isSendEnabled = uiState.isSendEnabled,
        snackbarHostState = snackbarHostState,
        onAction = viewModel::manageAction
    )
}

@Composable
fun ForgotPasswordScreenContent(
    email: String = "",
    isSendEnabled: Boolean = false,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    onAction: (ForgotPasswordAction) -> Unit = {}
) {
    AuthFormScaffold(
        title = stringResource(Res.string.forgot_password_title),
        snackbarHostState = snackbarHostState
    ) {
        Text(
            text = stringResource(Res.string.forgot_password_description),
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(Modifier.height(24.dp))
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = email,
            onValueChange = { onAction(ForgotPasswordAction.OnEmailChange(it)) },
            label = { Text(stringResource(Res.string.auth_email)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { onAction(ForgotPasswordAction.OnSendClick) })
        )
        Spacer(Modifier.height(24.dp))
        Button(
            enabled = isSendEnabled,
            onClick = { onAction(ForgotPasswordAction.OnSendClick) },
            modifier = Modifier.fillMaxWidth().height(48.dp)
        ) {
            Text(stringResource(Res.string.forgot_password_button))
        }
        TextButton(onClick = { onAction(ForgotPasswordAction.OnBackClick) }) {
            Text(stringResource(Res.string.forgot_password_back))
        }
    }
}

@PreviewLightDark
@Composable
private fun ForgotPasswordScreenPreview() {
    AppTheme { ForgotPasswordScreenContent(email = "usuario@ejemplo.com", isSendEnabled = true) }
}
