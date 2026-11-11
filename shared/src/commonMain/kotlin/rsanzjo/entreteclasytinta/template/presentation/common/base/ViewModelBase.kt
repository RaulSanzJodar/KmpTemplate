package rsanzjo.entreteclasytinta.template.presentation.common.base

import rsanzjo.entreteclasytinta.template.resources.*

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.updateAndGet
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException
import org.jetbrains.compose.resources.StringResource
import rsanzjo.entreteclasytinta.template.domain.error.DomainError
import rsanzjo.entreteclasytinta.template.presentation.common.navigation.NavHostEvent

/**
 * Base de todos los ViewModels y contrato MVI: el estado de pantalla [S] solo se modifica desde el
 * ViewModel con [setState], la vista únicamente lo observa y envía intenciones [A] a través de
 * [manageAction]. Los eventos de un solo uso para la vista (snackbars, etc.) salen por [effects]
 * con el tipo [E]; usa `Nothing` si la pantalla no tiene ninguno.
 */
abstract class ViewModelBase<S, A, E>(initialState: S) : ViewModel() {

    private val _uiState = MutableStateFlow(initialState)
    val uiState: StateFlow<S> = _uiState.asStateFlow()

    // Canal con buffer: un efecto emitido sin colector (p. ej. durante una rotación) no se pierde.
    private val _effects = Channel<E>(Channel.BUFFERED)
    val effects: Flow<E> = _effects.receiveAsFlow()

    protected val currentState: S get() = _uiState.value

    private val pendingLoads = MutableStateFlow(0)

    protected val _baseUiState = MutableStateFlow(BaseUiState())
    val baseUiState: StateFlow<BaseUiState> = _baseUiState.asStateFlow()

    // Canal con buffer: los eventos emitidos sin colector (p. ej. durante una rotación) no se pierden.
    private val _navHostEvent = Channel<NavHostEvent>(Channel.BUFFERED)
    val navHostEvent: Flow<NavHostEvent> = _navHostEvent.receiveAsFlow()

    abstract fun manageAction(action: A)

    protected fun setState(reducer: S.() -> S) {
        _uiState.update { it.reducer() }
    }

    protected fun sendEffect(effect: E) {
        _effects.trySend(effect)
    }

    fun launchCatching(
        onError: (Throwable) -> Unit = {},
        onFinish: () -> Unit = {},
        block: suspend CoroutineScope.() -> Unit
    ) = viewModelScope.launch(
        CoroutineExceptionHandler { _, throwable ->
            // TODO: Firebase.crashlytics.recordException(throwable)
            onError(throwable)
        },
        block = block
    ).invokeOnCompletion { onFinish() }

    /** Como [launchCatching], pero gestiona `isLoading` y limpia el error anterior antes de empezar. */
    fun launchWithLoading(
        onError: (Throwable) -> Unit = ::manageError,
        block: suspend CoroutineScope.() -> Unit
    ) {
        // Con operaciones solapadas, el loading solo se quita al terminar la última y el error
        // anterior solo se limpia al empezar la primera.
        if (pendingLoads.getAndUpdate { it + 1 } == 0) {
            _baseUiState.value = BaseUiState(isLoading = true)
        } else {
            _baseUiState.update { it.copy(isLoading = true) }
        }
        launchCatching(
            onError = onError,
            onFinish = {
                if (pendingLoads.updateAndGet { it - 1 } == 0) {
                    _baseUiState.update { it.copy(isLoading = false) }
                }
            },
            block = block
        )
    }

    fun navigateTo(navHostEvent: NavHostEvent) {
        _navHostEvent.trySend(navHostEvent)
    }

    fun manageError(error: Throwable) {
        // El error no debe apagar el loading mientras haya otras operaciones en curso.
        _baseUiState.update { error.toErrorState().copy(isLoading = pendingLoads.value > 0) }
    }
}

/** Estado de error a mostrar para [this]; también sirve para enviarlo como efecto de un solo uso. */
fun Throwable.toErrorState(): BaseUiState = when {
    this is DomainError.Http -> BaseUiState(
        errorMessageRes = Res.string.error_http_format,
        errorMessageArgs = listOf(code)
    )

    this is DomainError || this is CancellationException -> BaseUiState(errorMessageRes = toErrorMessageRes())

    message != null -> BaseUiState(errorMessage = message)

    else -> BaseUiState(errorMessageRes = Res.string.error_unknown)
}

/** Texto de error para [this]. */
fun Throwable.toErrorMessageRes(): StringResource = when (this) {
    is DomainError.Unauthorized -> Res.string.error_session_expired
    is DomainError.InvalidCredentials -> Res.string.auth_invalid_credential
    is DomainError.EmailNotConfirmed -> Res.string.auth_email_not_confirmed
    is DomainError.UserAlreadyExists -> Res.string.auth_user_already_exists
    is DomainError.WeakPassword -> Res.string.auth_weak_password
    is DomainError.TooManyRequests -> Res.string.auth_too_many_requests
    is DomainError.Network -> Res.string.network_exception
    is CancellationException -> Res.string.cancellation_exception
    else -> Res.string.error_unknown
}
