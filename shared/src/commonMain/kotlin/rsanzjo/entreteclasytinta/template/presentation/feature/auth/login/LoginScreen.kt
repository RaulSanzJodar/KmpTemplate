package rsanzjo.entreteclasytinta.template.presentation.feature.auth.login

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedButton
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
import rsanzjo.entreteclasytinta.template.domain.entity.SocialProvider
import rsanzjo.entreteclasytinta.template.presentation.common.auth.AuthFormScaffold
import rsanzjo.entreteclasytinta.template.presentation.common.auth.rememberNativeGoogleSignIn
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
    val nativeGoogleSignIn = rememberNativeGoogleSignIn(
        onError = { viewModel.manageAction(LoginAction.OnNativeSignInError) }
    )

    LaunchedEffect(Unit) { viewModel.navHostEvent.collect { event -> onNavEvent(event) } }

    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                is LoginEffect.ShowMessage -> snackbarHostState.showSnackbar(getString(effect.messageRes))
            }
        }
    }

    if (baseUiState.isLoading) {
        LoaderDialog()
    }

    LoginScreenContent(
        email = uiState.email,
        password = uiState.password,
        isLoginEnabled = uiState.isLoginEnabled,
        snackbarHostState = snackbarHostState,
        // Selector nativo si está disponible; si no, OAuth por navegador a través del ViewModel.
        onGoogleClick = nativeGoogleSignIn
            ?: { viewModel.manageAction(LoginAction.OnSocialClick(SocialProvider.Google)) },
        onAction = viewModel::manageAction
    )
}

@Composable
fun LoginScreenContent(
    email: String = "",
    password: String = "",
    isLoginEnabled: Boolean = false,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    onGoogleClick: () -> Unit = {},
    onAction: (LoginAction) -> Unit = {}
) {
    AuthFormScaffold(
        title = stringResource(Res.string.login_title),
        snackbarHostState = snackbarHostState
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = email,
            onValueChange = { onAction(LoginAction.OnEmailChange(it)) },
            label = { Text(stringResource(Res.string.auth_email)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next)
        )
        Spacer(Modifier.height(12.dp))
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = password,
            onValueChange = { onAction(LoginAction.OnPasswordChange(it)) },
            label = { Text(stringResource(Res.string.auth_password)) },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { onAction(LoginAction.OnLoginClick) })
        )
        TextButton(onClick = { onAction(LoginAction.OnForgotPasswordClick) }) {
            Text(stringResource(Res.string.login_forgot_password))
        }
        Spacer(Modifier.height(12.dp))
        Button(
            enabled = isLoginEnabled,
            onClick = { onAction(LoginAction.OnLoginClick) },
            modifier = Modifier.fillMaxWidth().height(48.dp)
        ) {
            Text(stringResource(Res.string.login_button))
        }
        TextButton(onClick = { onAction(LoginAction.OnRegisterClick) }) {
            Text(stringResource(Res.string.login_register))
        }

        Spacer(Modifier.height(12.dp))
        HorizontalDivider()
        Spacer(Modifier.height(24.dp))

        OutlinedButton(
            onClick = onGoogleClick,
            modifier = Modifier.fillMaxWidth().height(48.dp)
        ) {
            Text(stringResource(Res.string.login_google))
        }
        Spacer(Modifier.height(12.dp))
        OutlinedButton(
            onClick = { onAction(LoginAction.OnSocialClick(SocialProvider.Apple)) },
            modifier = Modifier.fillMaxWidth().height(48.dp)
        ) {
            Text(stringResource(Res.string.login_apple))
        }
    }
}

@PreviewLightDark
@Composable
private fun LoginScreenPreview() {
    AppTheme { LoginScreenContent(email = "usuario@ejemplo.com", isLoginEnabled = true) }
}
