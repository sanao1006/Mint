package me.sanao1006.screens

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen
import kotlinx.parcelize.Parcelize
import me.sanao1006.core.model.uistate.AntennaListUiState
import me.sanao1006.core.model.uistate.TimelineUiState
import me.sanao1006.screens.event.globalIcon.GlobalIconEvent
import me.sanao1006.screens.event.timeline.TimelineItemEvent

@Parcelize
data class AntennaListScreen(
    val antennaId: String
) : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    data class State (
        val uiState: AntennaListUiState,
        val timelineUiState: TimelineUiState,
        override val globalIconEventSink: (GlobalIconEvent) -> Unit,
        override val timelineEventSink: (TimelineItemEvent) -> Unit,
        override val pullToRefreshState: PullToRefreshState,
        override val isRefreshed: Boolean,
        override val onRefresh: () -> Unit,
        val eventSink: (Event) -> Unit
    ) : CircuitUiState, SubScreenState

    sealed class Event : CircuitUiEvent {
        data object OnDismissRequest : Event()
    }
}
