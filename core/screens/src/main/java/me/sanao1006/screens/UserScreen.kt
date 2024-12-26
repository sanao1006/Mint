package me.sanao1006.screens

import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen
import kotlinx.parcelize.Parcelize
import me.sanao1006.core.model.uistate.UserScreenUiState
import me.sanao1006.screens.event.globalIcon.GlobalIconEvent

@Parcelize
data class UserScreen(
    val userId: String,
    val userName: String? = null,
    val host: String? = null,
    val isFromDrawer: Boolean = false
) : Screen {
    data class State(
        val uiState: UserScreenUiState,
        val globalIconEventSink: (GlobalIconEvent) -> Unit,
        val eventSink: (Event) -> Unit
    ) : CircuitUiState

    sealed class Event : CircuitUiEvent {
        data object OnNotesCountClicked : Event()
        data object OnFollowersCountClicked : Event()
        data object OnFollowingCountClicked : Event()

        data object OnLoadingFailed : Event()
    }
}
