package me.sanao1006.screens

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshState
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.screen.Screen
import kotlinx.parcelize.Parcelize
import me.sanao1006.core.model.LoginUserInfo
import me.sanao1006.core.model.uistate.TimelineUiState
import me.sanao1006.screens.event.BottomAppBarActionEvent
import me.sanao1006.screens.event.DrawerEvent
import me.sanao1006.screens.event.GlobalIconEvent
import me.sanao1006.screens.event.NoteCreateEvent
import me.sanao1006.screens.event.TimelineItemEvent

@Parcelize
data object HomeScreen : Screen {
    @OptIn(ExperimentalMaterialApi::class)
    data class State(
        val timelineUiState: TimelineUiState,
        val navigator: Navigator,
        val pullToRefreshState: PullRefreshState,
        val isRefreshed: Boolean = false,
        val drawerUserInfo: LoginUserInfo,
        val timelineEventSink: (TimelineItemEvent) -> Unit,
        val noteCreateEventSink: (NoteCreateEvent) -> Unit,
        val drawerEventSink: (DrawerEvent) -> Unit,
        val globalIconEventSink: (GlobalIconEvent) -> Unit,
        val bottomAppBarEventSInk: (BottomAppBarActionEvent) -> Unit,
        val eventSink: (Event) -> Unit
    ) : CircuitUiState

    sealed class Event : CircuitUiEvent {
        data object OnDismissRequest : Event()
        sealed class TimelineEvent : Event() {
            data object OnLocalTimelineClicked : TimelineEvent()
            data object OnSocialTimelineClicked : TimelineEvent()
            data object OnGlobalTimelineClicked : TimelineEvent()
        }
    }
}
