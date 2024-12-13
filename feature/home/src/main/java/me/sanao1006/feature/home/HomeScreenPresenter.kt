package me.sanao1006.feature.home

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
import com.slack.circuit.foundation.rememberAnsweringNavigator
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuitx.effects.LaunchedImpressionEffect
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.sanao1006.core.domain.home.GetNotesTimelineUseCase
import me.sanao1006.core.domain.home.TimelineType
import me.sanao1006.core.domain.home.UpdateAccountUseCase
import me.sanao1006.core.model.LoginUserInfo
import me.sanao1006.screens.HomeScreen
import me.sanao1006.screens.NoteScreen
import me.sanao1006.screens.event.TimelineItemAction
import me.sanao1006.screens.event.TimelineItemEvent
import me.sanao1006.screens.event.handleBottomAppBarActionEvent
import me.sanao1006.screens.event.handleDrawerEvent
import me.sanao1006.screens.event.handleNavigationIconClicked
import me.sanao1006.screens.event.handleNoteCreateEvent
import me.sanao1006.screens.event.handleTimelineItemIconClicked
import me.sanao1006.screens.event.handleTimelineItemReplyClicked
import me.sanao1006.screens.uiState.TimelineUiState

class HomeScreenPresenter @AssistedInject constructor(
    @Assisted private val navigator: Navigator,
    private val getNotesTimelineUseCase: GetNotesTimelineUseCase,
    private val updateMyAccountUseCase: UpdateAccountUseCase
) : Presenter<HomeScreen.State> {

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    override fun present(): HomeScreen.State {
        var isSuccessCreateNote: Boolean? by rememberRetained { mutableStateOf(null) }
        var loginUserInfo: LoginUserInfo by rememberRetained {
            mutableStateOf(
                LoginUserInfo()
            )
        }
        val context = LocalContext.current
        val scope = rememberCoroutineScope()
        val nav = rememberAnsweringNavigator<NoteScreen.Result>(navigator) { result ->
            isSuccessCreateNote = result.success
        }

        var timelineType by rememberRetained { mutableStateOf(TimelineType.SOCIAL) }
        var timelineUiState: TimelineUiState by rememberRetained(timelineType) {
            mutableStateOf(TimelineUiState())
        }

        var isRefreshed by remember { mutableStateOf(false) }
        val pullRefreshState = rememberPullRefreshState(
            refreshing = isRefreshed,
            onRefresh = {
                scope.launch {
                    isRefreshed = true
                    timelineUiState.timelineItems = getNotesTimelineUseCase(timelineType)
                    delay(1500L)
                    isRefreshed = false
                }
            },
            refreshThreshold = 50.dp,
            refreshingOffset = 50.dp
        )
        LaunchedImpressionEffect {
            timelineUiState.timelineItems = getNotesTimelineUseCase(timelineType)
            loginUserInfo = updateMyAccountUseCase()
        }

        LaunchedImpressionEffect(timelineType) {
            timelineUiState.timelineItems = getNotesTimelineUseCase(timelineType)
        }

        return HomeScreen.State(
            timelineUiState = timelineUiState,
            navigator = navigator,
            pullToRefreshState = pullRefreshState,
            isRefreshed = isRefreshed,
            drawerUserInfo = loginUserInfo,
            noteCreateEventSink = { event ->
                event.handleNoteCreateEvent(
                    isSuccessCreateNote,
                    context,
                    nav
                )
            },
            timelineEventSink = { event ->
                when (event) {
                    is TimelineItemEvent.OnTimelineItemIconClicked ->
                        event.handleTimelineItemIconClicked(navigator)

                    is TimelineItemEvent.OnTimelineItemReplyClicked ->
                        event.handleTimelineItemReplyClicked(navigator)

                    is TimelineItemEvent.OnTimelineItemRepostClicked -> {
                        timelineUiState =
                            timelineUiState.copy(
                                showBottomSheet = true,
                                timelineAction = TimelineItemAction.Renote
                            )
                    }

                    is TimelineItemEvent.OnTimelineItemReactionClicked -> {}

                    is TimelineItemEvent.OnTimelineItemOptionClicked -> {
                        timelineUiState =
                            timelineUiState.copy(
                                showBottomSheet = true,
                                timelineAction = TimelineItemAction.Option
                            )
                    }
                }
            },
            drawerEventSink = { event -> event.handleDrawerEvent(navigator, loginUserInfo) },
            bottomAppBarEventSInk = { event -> event.handleBottomAppBarActionEvent(navigator) },
            globalIconEventSink = { event -> event.handleNavigationIconClicked(navigator) }
        ) { event ->
            when (event) {
                is HomeScreen.Event.TimelineEvent -> handleTimelineEvent(event) {
                    timelineType = it
                }

                HomeScreen.Event.OnDismissRequest -> {
                    timelineUiState = timelineUiState.copy(showBottomSheet = false)
                }
            }
        }
    }
}

@CircuitInject(HomeScreen::class, SingletonComponent::class)
@AssistedFactory
fun interface Factory {
    fun create(navigator: Navigator): HomeScreenPresenter
}
