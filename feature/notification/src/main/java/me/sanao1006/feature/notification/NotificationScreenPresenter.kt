package me.sanao1006.feature.notification

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuitx.effects.LaunchedImpressionEffect
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.sanao1006.core.domain.notification.GetNotificationsUseCase
import me.sanao1006.core.model.uistate.NotificationUiState
import me.sanao1006.core.model.uistate.NotificationUiStateObject
import me.sanao1006.screens.NotificationScreen
import me.sanao1006.screens.event.bottomAppBar.BottomAppBarPresenter
import me.sanao1006.screens.event.drawer.DrawerEventPresenter
import me.sanao1006.screens.event.globalIcon.GlobalIconEventPresenter
import me.sanao1006.screens.event.notecreate.NoteCreatePresenter
import me.sanao1006.screens.event.timeline.TimelineEventPresenter
import javax.inject.Inject

@CircuitInject(NotificationScreen::class, SingletonComponent::class)
class NotificationScreenPresenter @Inject constructor(
    private val getNotificationsUseCase: GetNotificationsUseCase,
    private val bottomAppBarPresenter: BottomAppBarPresenter,
    private val timelineEventPresenter: TimelineEventPresenter,
    private val drawerEventPresenter: DrawerEventPresenter,
    private val globalIconEventPresenter: GlobalIconEventPresenter,
    private val noteCreatePresenter: NoteCreatePresenter
) : Presenter<NotificationScreen.State> {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun present(): NotificationScreen.State {
        val bottomAppBarState = bottomAppBarPresenter.present()
        val timelineEventState = timelineEventPresenter.present()
        val drawerEventState = drawerEventPresenter.present()
        val globalIconEventState = globalIconEventPresenter.present()
        val noteCreateState = noteCreatePresenter.present()

        val scope = rememberCoroutineScope()
        var notificationUiState: NotificationUiState by rememberRetained {
            mutableStateOf(NotificationUiState())
        }

        var isRefreshed by remember { mutableStateOf(false) }
        val pullRefreshState = rememberPullToRefreshState()

        LaunchedImpressionEffect(Unit) {
            notificationUiState = fetchNotificationItems(
                uiState = notificationUiState,
                getItems = { getNotificationsUseCase.invoke().notificationUiStateObjects },
                setSuccessLoading = { timelineEventState.setSuccessLoading(it) }
            )
        }
        return NotificationScreen.State(
            notificationUiState = notificationUiState,
            expandDialog = drawerEventState.expandDialog,
            timelineUiState = timelineEventState.uiState,
            isSuccessCreateNote = noteCreateState.isSuccessCreateNote,
            drawerUserInfo = drawerEventState.loginUserInfo,
            pullToRefreshState = pullRefreshState,
            isRefreshed = isRefreshed,
            timelineEventSink = timelineEventState.eventSink,
            noteCreateEventSink = noteCreateState.eventSink,
            drawerEventSink = drawerEventState.eventSink,
            globalIconEventSink = globalIconEventState.eventSink,
            bottomAppBarEventSink = bottomAppBarState.eventSink,
            onRefresh = {
                scope.launch {
                    isRefreshed = true
                    notificationUiState = fetchNotificationItems(
                        uiState = notificationUiState,
                        getItems = { getNotificationsUseCase.invoke().notificationUiStateObjects },
                        setSuccessLoading = { timelineEventState.setSuccessLoading(it) }
                    )
                    delay(1500L)
                    isRefreshed = false
                }
            },
            eventSink = { event ->
                when (event) {
                    NotificationScreen.Event.OnDismissRequest -> {
                        timelineEventState.setShowBottomSheet(false)
                    }
                }
            }
        )
    }
}

private suspend fun fetchNotificationItems(
    uiState: NotificationUiState,
    getItems: suspend () -> List<NotificationUiStateObject>,
    setSuccessLoading: (Boolean) -> Unit
): NotificationUiState {
    val notificationItems: List<NotificationUiStateObject> = getItems()
    return if (notificationItems.isEmpty()) {
        setSuccessLoading(false)
        uiState.copy(notificationUiStateObjects = emptyList())
    } else {
        setSuccessLoading(true)
        uiState.copy(notificationUiStateObjects = notificationItems)
    }
}
