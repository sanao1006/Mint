package me.sanao1006.screens

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshState
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen
import kotlinx.parcelize.Parcelize
import me.sanao1006.core.model.LoginUserInfo
import me.sanao1006.core.model.uistate.NotificationUiState
import me.sanao1006.core.model.uistate.TimelineUiState
import me.sanao1006.screens.event.NoteCreateEvent
import me.sanao1006.screens.event.bottomAppBar.BottomAppBarActionEvent
import me.sanao1006.screens.event.drawer.DrawerEvent
import me.sanao1006.screens.event.globalIcon.GlobalIconEvent
import me.sanao1006.screens.event.timeline.TimelineItemEvent

@Parcelize
data object NotificationScreen : Screen {
    @OptIn(ExperimentalMaterialApi::class)
    data class State(
        val notificationUiState: NotificationUiState,
        override val timelineUiState: TimelineUiState,
        override val drawerUserInfo: LoginUserInfo,
        override val pullToRefreshState: PullRefreshState,
        override val isRefreshed: Boolean,
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
