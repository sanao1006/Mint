package me.sanao1006.feature.notification

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuitx.effects.LaunchedImpressionEffect
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.sanao1006.core.data.compositionLocal.LocalNavigator
import me.sanao1006.core.domain.home.UpdateAccountUseCase
import me.sanao1006.core.domain.notification.GetNotificationsUseCase
import me.sanao1006.core.model.LoginUserInfo
import me.sanao1006.core.model.uistate.NotificationUiState
import me.sanao1006.screens.NotificationScreen
import me.sanao1006.screens.event.BottomAppBarPresenter
import me.sanao1006.screens.event.TimelineEventPresenter
import me.sanao1006.screens.event.handleDrawerEvent
import me.sanao1006.screens.event.handleNavigationIconClicked
import me.sanao1006.screens.event.handleNoteCreateEvent

@CircuitInject(NotificationScreen::class, SingletonComponent::class)
class NotificationScreenPresenter @Inject constructor(
    private val updateMyAccountUseCase: UpdateAccountUseCase,
    private val getNotificationsUseCase: GetNotificationsUseCase,
    private val bottomAppBarPresenter: BottomAppBarPresenter,
    private val timelineEventPresenter: TimelineEventPresenter
) : Presenter<NotificationScreen.State> {
    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    override fun present(): NotificationScreen.State {
        val navigator = LocalNavigator.current
        val bottomAppBarState = bottomAppBarPresenter.present()
        val timelineEventState = timelineEventPresenter.present()
        var isSuccessCreateNote: Boolean? by rememberRetained { mutableStateOf(null) }
        var loginUserInfo: LoginUserInfo by rememberRetained {
            mutableStateOf(
                LoginUserInfo()
            )
        }

        val context = LocalContext.current
        val scope = rememberCoroutineScope()

        var notificationUiState: NotificationUiState by rememberRetained {
            mutableStateOf(NotificationUiState())
        }

        var isRefreshed by remember { mutableStateOf(false) }
        val pullRefreshState = rememberPullRefreshState(
            refreshing = isRefreshed,
            onRefresh = {
                scope.launch {
                    isRefreshed = true
                    notificationUiState = getNotificationsUseCase()
                    delay(1500L)
                    isRefreshed = false
                }
            },
            refreshThreshold = 50.dp,
            refreshingOffset = 50.dp
        )

        LaunchedImpressionEffect(Unit) {
            loginUserInfo = updateMyAccountUseCase()
            notificationUiState = getNotificationsUseCase()
        }
        return NotificationScreen.State(
            notificationUiState = notificationUiState,
            timelineUiState = timelineEventState.uiState,
            isSuccessCreateNote = isSuccessCreateNote,
            navigator = navigator,
            drawerUserInfo = loginUserInfo,
            pullToRefreshState = pullRefreshState,
            isRefreshed = isRefreshed,
            timelineEventSink = timelineEventState.eventSink,
            noteCreateEventSink = { event ->
                event.handleNoteCreateEvent(
                    isSuccessCreateNote,
                    context,
                    navigator
                )
            },
            drawerEventSink = { event -> event.handleDrawerEvent(navigator, loginUserInfo) },
            globalIconEventSink = { event -> event.handleNavigationIconClicked(navigator) },
            bottomAppBarEventSink = bottomAppBarState.eventSink,
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
