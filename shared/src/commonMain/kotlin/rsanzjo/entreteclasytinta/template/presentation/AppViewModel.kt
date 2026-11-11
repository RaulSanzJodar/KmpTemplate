package rsanzjo.entreteclasytinta.template.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import rsanzjo.entreteclasytinta.template.domain.entity.SessionState
import rsanzjo.entreteclasytinta.template.domain.useCase.AuthUseCase

/**
 * ViewModel raíz de la app: expone el estado de la sesión, que es lo que decide en `App` si se
 * muestra la zona de autenticación o la zona privada. Ninguna pantalla navega entre ambas a mano.
 */
class AppViewModel(authUseCase: AuthUseCase) : ViewModel() {

    val sessionState: StateFlow<SessionState> = authUseCase.sessionState
        .stateIn(viewModelScope, SharingStarted.Eagerly, SessionState.Loading)
}
