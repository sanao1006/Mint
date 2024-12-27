package me.sanao1006.screens

import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen
import kotlinx.parcelize.Parcelize
import me.sanao1006.core.model.uistate.AnnouncementUiState

@Parcelize
object AntennaScreen : Screen {
    data class State(
        val uiState: AnnouncementUiState,
        val eventSink: (Event) -> Unit
    ) : CircuitUiState

    sealed class Event : CircuitUiEvent {
        data class OnAntennaClick(val id: String) : Event()
        data object OnEditClick : Event()
        data object OnCreateClick : Event()
        data object OnBackClick : Event()
    }
}
