package me.sanao1006.screens

import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.Immutable
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.screen.Screen
import kotlinx.coroutines.CoroutineScope
import kotlinx.parcelize.Parcelize

@Parcelize
data object HomeScreen : Screen {
    @Immutable
    data class State(
        val uiState: List<me.sanao1006.core.model.notes.TimelineUiState?> = listOf(),
        val navigator: Navigator,
        val isSuccessCreateNote: Boolean? = null,
        val eventSink: (Event) -> Unit
    ) : CircuitUiState

    sealed class Event : CircuitUiEvent {
        data class OnNoteCreated(
            val snackbarHostState: SnackbarHostState,
            val scope: CoroutineScope
        ) : Event()

        data object OnLocalTimelineClicked : Event()
        data object OnSocialTimelineClicked : Event()
        data object OnGlobalTimelineClicked : Event()
        data object OnNoteCreateClicked : Event()
    }
}
