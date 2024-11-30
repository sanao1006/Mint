package me.sanao1006.screens

import androidx.compose.runtime.Immutable
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen
import kotlinx.parcelize.Parcelize
import me.sanao1006.core.model.home.notes.TimelineUiState

@Parcelize
data object HomeScreen : Screen {
    @Immutable
    data class State(
        val uiState: List<TimelineUiState?> = listOf(),
        val eventSink: (Event) -> Unit
    ) : CircuitUiState

    sealed class Event : CircuitUiEvent {
        data object OnLocalTimelineClicked : Event()
        data object OnSocialTimelineClicked : Event()
        data object OnGlobalTimelineClicked : Event()
        data object OnNoteCreateClicked : Event()
    }
}
