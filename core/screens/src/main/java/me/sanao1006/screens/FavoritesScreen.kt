package me.sanao1006.screens

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen
import kotlinx.parcelize.Parcelize
import me.sanao1006.core.model.uistate.FavoritesScreenUiState
import me.sanao1006.core.model.uistate.TimelineUiState
import me.sanao1006.screens.event.globalIcon.GlobalIconEvent
import me.sanao1006.screens.event.timeline.TimelineItemEvent

@Parcelize
object FavoritesScreen : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    data class State(
        val favoritesScreenUiState: FavoritesScreenUiState,
        val timelineUiState: TimelineUiState,
        override val pullToRefreshState: PullToRefreshState,
        override val isRefreshed: Boolean = false,
        override val onRefresh: () -> Unit,
        override val timelineEventSink: (TimelineItemEvent) -> Unit,
        override val globalIconEventSink: (GlobalIconEvent) -> Unit,
        val eventSink: (Event) -> Unit
    ) : CircuitUiState, SubScreenState

    sealed class Event : CircuitUiEvent {
        data object OnDismissRequest : Event()
    }
}
