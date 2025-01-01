package me.sanao1006.feature.home

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuitx.effects.LaunchedImpressionEffect
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.sanao1006.core.domain.home.GetNotesTimelineUseCase
import me.sanao1006.core.domain.home.TimelineType
import me.sanao1006.core.model.notes.TimelineItem
import me.sanao1006.core.model.uistate.HomeScreenUiState
import me.sanao1006.screens.HomeScreen
import me.sanao1006.screens.event.bottomAppBar.BottomAppBarPresenter
import me.sanao1006.screens.event.drawer.DrawerEventPresenter
import me.sanao1006.screens.event.globalIcon.GlobalIconEventPresenter
import me.sanao1006.screens.event.notecreate.NoteCreatePresenter
import me.sanao1006.screens.event.timeline.TimelineEventPresenter

@CircuitInject(HomeScreen::class, SingletonComponent::class)
class HomeScreenPresenter @Inject constructor(
    private val getNotesTimelineUseCase: GetNotesTimelineUseCase,
    private val bottomAppBarPresenter: BottomAppBarPresenter,
    private val timelineEventPresenter: TimelineEventPresenter,
    private val drawerEventPresenter: DrawerEventPresenter,
    private val globalIconEventPresenter: GlobalIconEventPresenter,
    private val noteCreatePresenter: NoteCreatePresenter
) : Presenter<HomeScreen.State> {

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    override fun present(): HomeScreen.State {
        val bottomAppBarPresenter = bottomAppBarPresenter.present()
        val timelineEventPresenter = timelineEventPresenter.present()
        val drawerEventState = drawerEventPresenter.present()
        val globalIconEventState = globalIconEventPresenter.present()
        val noteCreateState = noteCreatePresenter.present()

        val scope = rememberCoroutineScope()
        var timelineType by rememberRetained { mutableStateOf(TimelineType.SOCIAL) }
        var homeScreenUiState: HomeScreenUiState by rememberRetained(timelineType) {
            mutableStateOf(HomeScreenUiState())
        }

        var isRefreshed by remember { mutableStateOf(false) }
        val pullRefreshState = rememberPullRefreshState(
            refreshing = isRefreshed,
            onRefresh = {
                scope.launch {
                    isRefreshed = true
                    homeScreenUiState = fetchTimelineItems(
                        timelineType = timelineType,
                        uiState = homeScreenUiState,
                        setSuccessLoading = { timelineEventPresenter.setSuccessLoading(it) },
                        getItems = { getNotesTimelineUseCase(it) }
                    )
                    delay(1000L)
                    isRefreshed = false
                }
            },
            refreshThreshold = 50.dp,
            refreshingOffset = 50.dp
        )
        LaunchedImpressionEffect {
            homeScreenUiState = fetchTimelineItems(
                timelineType = timelineType,
                uiState = homeScreenUiState,
                setSuccessLoading = { timelineEventPresenter.setSuccessLoading(it) },
                getItems = { getNotesTimelineUseCase(it) }
            )
        }

        LaunchedImpressionEffect(timelineType) {
            homeScreenUiState = fetchTimelineItems(
                timelineType = timelineType,
                uiState = homeScreenUiState,
                setSuccessLoading = { timelineEventPresenter.setSuccessLoading(it) },
                getItems = { getNotesTimelineUseCase(it) }
            )
        }

        return HomeScreen.State(
            homeScreenUiState = homeScreenUiState,
            timelineUiState = timelineEventPresenter.uiState,
            expandDialog = drawerEventState.expandDialog,
            isSuccessCreateNote = noteCreateState.isSuccessCreateNote,
            pullToRefreshState = pullRefreshState,
            isRefreshed = isRefreshed,
            drawerUserInfo = drawerEventState.loginUserInfo,
            noteCreateEventSink = noteCreateState.eventSink,
            timelineEventSink = timelineEventPresenter.eventSink,
            drawerEventSink = drawerEventState.eventSink,
            bottomAppBarEventSink = bottomAppBarPresenter.eventSink,
            globalIconEventSink = globalIconEventState.eventSink
        ) { event ->
            when (event) {
                HomeScreen.Event.TimelineEvent.OnLocalTimelineClicked ->
                    timelineType = TimelineType.HOME

                HomeScreen.Event.TimelineEvent.OnSocialTimelineClicked
                -> timelineType = TimelineType.SOCIAL

                HomeScreen.Event.TimelineEvent.OnGlobalTimelineClicked ->
                    timelineType = TimelineType.GLOBAL

                HomeScreen.Event.OnDismissRequest -> {
                    timelineEventPresenter.setShowBottomSheet(false)
                }
            }
        }
    }
}

private suspend fun fetchTimelineItems(
    timelineType: TimelineType,
    uiState: HomeScreenUiState,
    getItems: suspend (TimelineType) -> List<TimelineItem>,
    setSuccessLoading: (Boolean) -> Unit
): HomeScreenUiState {
    val timelineItems: List<TimelineItem> = getItems(timelineType)
    return if (timelineItems.isEmpty()) {
        setSuccessLoading(false)
        uiState.copy(timelineItems = emptyList())
    } else {
        setSuccessLoading(true)
        uiState.copy(timelineItems = timelineItems)
    }
}
