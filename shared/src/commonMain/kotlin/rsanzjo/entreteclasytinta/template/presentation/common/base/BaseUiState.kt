package rsanzjo.entreteclasytinta.template.presentation.common.base

import org.jetbrains.compose.resources.StringResource

import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import org.jetbrains.compose.resources.getString

data class BaseUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val errorMessageRes: StringResource? = null,
    val errorMessageArgs: List<Any> = emptyList()
) {
    val hasError: Boolean get() = errorMessage != null || errorMessageRes != null
}

/** Texto del error ya resuelto (a partir de `strings.xml` si hace falta). */
suspend fun BaseUiState.resolveErrorText(): String? =
    errorMessage ?: errorMessageRes?.let { getString(it, *errorMessageArgs.toTypedArray()) }

@Composable
fun BaseUiState.errorText(): String? =
    produceState<String?>(initialValue = errorMessage, this) { value = resolveErrorText() }.value
