package me.sanao1006.screens

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen
import kotlinx.parcelize.Parcelize
import me.sanao1006.core.model.LoginUserInfo
import me.sanao1006.core.model.uistate.HomeScreenUiState
import me.sanao1006.core.model.uistate.TimelineUiState
import me.sanao1006.screens.event.bottomAppBar.BottomAppBarActionEvent
import me.sanao1006.screens.event.drawer.DrawerEvent
import me.sanao1006.screens.event.globalIcon.GlobalIconEvent
import me.sanao1006.screens.event.notecreate.NoteCreateEvent
import me.sanao1006.screens.event.timeline.TimelineItemEvent

@Parcelize
data object HomeScreen : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    data class State(
        val homeScreenUiState: HomeScreenUiState,
        val expandDialog: Boolean,
        override val timelineUiState: TimelineUiState,
        override val isSuccessCreateNote: Boolean?,
        override val pullToRefreshState: PullToRefreshState,
        override val isRefreshed: Boolean = false,
        override val drawerUserInfo: LoginUserInfo,
        override val onRefresh: () -> Unit,
        override val timelineEventSink: (TimelineItemEvent) -> Unit,
        override val noteCreateEventSink: (NoteCreateEvent) -> Unit,
        override val drawerEventSink: (DrawerEvent) -> Unit,
        override val globalIconEventSink: (GlobalIconEvent) -> Unit,
        override val bottomAppBarEventSink: (BottomAppBarActionEvent) -> Unit,
        val eventSink: (Event) -> Unit
    ) : CircuitUiState, MainScreenState

    sealed class Event : CircuitUiEvent {
        data object OnDismissRequest : Event()
        sealed class TimelineEvent : Event() {
            data object OnLocalTimelineClicked : TimelineEvent()
            data object OnSocialTimelineClicked : TimelineEvent()
            data object OnGlobalTimelineClicked : TimelineEvent()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
interface MainScreenState {
    val timelineUiState: TimelineUiState
    val isSuccessCreateNote: Boolean?
    val pullToRefreshState: PullToRefreshState
    val isRefreshed: Boolean
    val drawerUserInfo: LoginUserInfo
    val onRefresh: () -> Unit
    val timelineEventSink: (TimelineItemEvent) -> Unit
    val noteCreateEventSink: (NoteCreateEvent) -> Unit
    val drawerEventSink: (DrawerEvent) -> Unit
    val globalIconEventSink: (GlobalIconEvent) -> Unit
    val bottomAppBarEventSink: (BottomAppBarActionEvent) -> Unit
}

@OptIn(ExperimentalMaterial3Api::class)
interface SubScreenState {
    val pullToRefreshState: PullToRefreshState
    val isRefreshed: Boolean
    val onRefresh: () -> Unit
    val timelineEventSink: (TimelineItemEvent) -> Unit
    val globalIconEventSink: (GlobalIconEvent) -> Unit
}
