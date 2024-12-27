package me.sanao1006.screens

import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen
import kotlinx.parcelize.Parcelize
import me.sanao1006.core.model.uistate.AnnouncementUiState

@Parcelize
object AntennaScreen : Screen {
    data class State(
        val uiState: AnnouncementUiState
    ) : CircuitUiState

    data class Event(
        val onAntennaClick: (String) -> Unit,
        val onEditClick: (String) -> Unit,
        val onCreateClick: () -> Unit
    ) : CircuitUiEvent
}