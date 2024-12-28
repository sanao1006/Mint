package me.sanao1006.screens

import androidx.compose.material3.SnackbarHostState
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.PopResult
import com.slack.circuit.runtime.screen.Screen
import kotlinx.parcelize.Parcelize
import me.sanao1006.core.model.antenna.Antenna
import me.sanao1006.core.model.uistate.AntennaScreenUiState
import me.sanao1006.screens.event.globalIcon.GlobalIconEvent

@Parcelize
object AntennaScreen : Screen {
    data class State(
        val uiState: AntennaScreenUiState,
        val isSuccessPostAntenna: Boolean?,
        val screenName: String,
        val openDialog: Boolean,
        val selectedAntennaId: String?,
        val globalIconEventSink: (GlobalIconEvent) -> Unit,
        val eventSink: (Event) -> Unit
    ) : CircuitUiState

    sealed class Event : CircuitUiEvent {
        data class OnAntennaClick(val id: String) : Event()
        data class OnEditClick(
            val antenna: Antenna
        ) : Event()

        data class OnDeleteButtonClick(val id: String) : Event()
        data class OnDeleteClick(val id: String) : Event()
        data object OnDialogHideClick : Event()
        data object OnCreateClick : Event()
        data class OnAntennaPost(
            val snackbarHostState: SnackbarHostState
        ) : Event()
    }

    @Parcelize
    data class Result(
        val success: Boolean,
        val screenName: String
    ) : PopResult
}
