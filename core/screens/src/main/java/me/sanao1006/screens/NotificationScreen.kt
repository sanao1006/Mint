package me.sanao1006.screens

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshState
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.screen.Screen
import kotlinx.parcelize.Parcelize
import me.sanao1006.core.model.LoginUserInfo
import me.sanao1006.core.model.uistate.NotificationUiState
import me.sanao1006.screens.event.BottomAppBarActionEvent
import me.sanao1006.screens.event.DrawerEvent
import me.sanao1006.screens.event.GlobalIconEvent
import me.sanao1006.screens.event.NoteCreateEvent
import me.sanao1006.screens.event.TimelineItemEvent

@Parcelize
data object NotificationScreen : Screen {
    @OptIn(ExperimentalMaterialApi::class)
    data class State(
        val notificationUiState: NotificationUiState,
        val isSuccessCreateNote: Boolean?,
        val navigator: Navigator,
        val drawerUserInfo: LoginUserInfo,
        val pullToRefreshState: PullRefreshState,
        val isRefreshed: Boolean,
        val timelineEventSink: (TimelineItemEvent) -> Unit,
        val noteCreateEventSink: (NoteCreateEvent) -> Unit,
        val drawerEventSink: (DrawerEvent) -> Unit,
        val globalIconEventSink: (GlobalIconEvent) -> Unit,
        val bottomAppBarEventSInk: (BottomAppBarActionEvent) -> Unit,
        val eventSink: (Event) -> Unit
    ) : CircuitUiState

    sealed class Event : CircuitUiState {
        data object OnDismissRequest : Event()
    }
}
