package me.sanao1006.screens

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen
import kotlinx.parcelize.Parcelize
import me.sanao1006.core.model.LoginUserInfo
import me.sanao1006.core.model.uistate.NotificationUiState
import me.sanao1006.core.model.uistate.TimelineUiState
import me.sanao1006.screens.event.bottomAppBar.BottomAppBarActionEvent
import me.sanao1006.screens.event.drawer.DrawerEvent
import me.sanao1006.screens.event.globalIcon.GlobalIconEvent
import me.sanao1006.screens.event.notecreate.NoteCreateEvent
import me.sanao1006.screens.event.timeline.TimelineItemEvent

@Parcelize
data object NotificationScreen : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    data class State(
        val notificationUiState: NotificationUiState,
        val expandDialog: Boolean,
        override val timelineUiState: TimelineUiState,
        override val isSuccessCreateNote: Boolean?,
        override val drawerUserInfo: LoginUserInfo,
        override val pullToRefreshState: PullToRefreshState,
        override val isRefreshed: Boolean,
        override val onRefresh: () -> Unit,
        override val timelineEventSink: (TimelineItemEvent) -> Unit,
        override val noteCreateEventSink: (NoteCreateEvent) -> Unit,
        override val drawerEventSink: (DrawerEvent) -> Unit,
        override val globalIconEventSink: (GlobalIconEvent) -> Unit,
        override val bottomAppBarEventSink: (BottomAppBarActionEvent) -> Unit,
        val eventSink: (Event) -> Unit
    ) : CircuitUiState, MainScreenState

    sealed class Event : CircuitUiState {
        data object OnDismissRequest : Event()
    }
}
