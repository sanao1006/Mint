package me.sanao1006.screens

import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen
import kotlinx.parcelize.Parcelize
import me.sanao1006.core.model.uistate.AnnouncementUiState
import me.sanao1006.screens.event.globalIcon.GlobalIconEvent

@Parcelize
object AnnouncementScreen : Screen {
    data class State(
        val uiState: AnnouncementUiState,
        val selectedTabIndex: Int,
        val announcementItemExpandedStates: Map<String, Boolean>,
        val globalIconEventSink: (GlobalIconEvent) -> Unit,
        val eventSink: (Event) -> Unit
    ) : CircuitUiState

    sealed class Event : CircuitUiEvent {
        data class OnTabClicked(val index: Int) : Event()
        data class OnCardClicked(val id: String) : Event()
    }
}
