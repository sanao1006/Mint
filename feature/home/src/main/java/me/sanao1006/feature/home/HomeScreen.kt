package me.sanao1006.feature.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.zIndex
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuitx.effects.LaunchedImpressionEffect
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.launch
import me.sanao1006.core.ui.MainScreenDrawerWrapper
import me.sanao1006.core.ui.MainScreenTimelineContentBox
import me.sanao1006.core.ui.TimelineColumn
import me.sanao1006.screens.HomeScreen
import me.sanao1006.screens.MainScreenType
import me.sanao1006.screens.event.globalIcon.GlobalIconEvent
import me.sanao1006.screens.event.notecreate.NoteCreateEvent
import me.sanao1006.screens.event.timeline.TimelineItemEvent

@CircuitInject(HomeScreen::class, SingletonComponent::class)
@Composable
fun HomeScreenUi(state: HomeScreen.State, modifier: Modifier) {
    Box(modifier = modifier.fillMaxSize()) {
        val pagerState = rememberPagerState(initialPage = 1) { 3 }
        val scope = rememberCoroutineScope()
        val drawerState = rememberDrawerState(DrawerValue.Closed)
        val snackbarHostState = remember { SnackbarHostState() }
        LaunchedImpressionEffect(state.isSuccessCreateNote) {
            state.noteCreateEventSink(
                NoteCreateEvent.OnNoteCreated(
                    snackbarHostState = snackbarHostState,
                    scope = scope
                )
            )
        }

        MainScreenDrawerWrapper(
            loginUserInfo = state.drawerUserInfo,
            drawerState = drawerState,
            scope = scope,
            event = state.drawerEventSink
        ) {
            HomeScreenUiContent(
                state = state,
                pagerState = pagerState,
                modifier = Modifier,
                snackbarHostState = snackbarHostState,
                onGlobalIconClicked = {
                    state.globalIconEventSink(
                        GlobalIconEvent.OnGlobalIconClicked(
                            drawerState,
                            scope
                        )
                    )
                },
                onHomeClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(0)
                        state.eventSink(HomeScreen.Event.TimelineEvent.OnLocalTimelineClicked)
                    }
                },
                onSocialClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(1)
                        state.eventSink(HomeScreen.Event.TimelineEvent.OnSocialTimelineClicked)
                    }
                },
                onGlobalClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(2)
                        state.eventSink(HomeScreen.Event.TimelineEvent.OnGlobalTimelineClicked)
                    }
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun HomeScreenUiContent(
    state: HomeScreen.State,
    pagerState: PagerState,
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState,
    onGlobalIconClicked: () -> Unit,
    onHomeClick: () -> Unit,
    onSocialClick: () -> Unit,
    onGlobalClick: () -> Unit
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            HomeScreenTopAppBar(
                topAppBarTimelineState = TopAppBarTimelineState.get(pagerState.currentPage),
                onNavigationIconClick = onGlobalIconClicked,
                onHomeClick = onHomeClick,
                onSocialClick = onSocialClick,
                onGlobalClick = onGlobalClick
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) {
        MainScreenTimelineContentBox(
            state = state,
            mainScreenType = MainScreenType.HOME,
            snackbarHostState = snackbarHostState,
            pullRefreshState = state.pullToRefreshState,
            isRefreshed = state.isRefreshed,
            modifier = Modifier.padding(it),
            contentLoadingState = state.timelineUiState.isSuccessLoading,
            isEmptyContent = state.homeScreenUiState.timelineItems.isEmpty()
        ) {
            TimelineColumn(
                state = state,
                pagerState = pagerState,
                modifier = Modifier.zIndex(0f)
            )
        }
    }
}

@Composable
private fun TimelineColumn(
    state: HomeScreen.State,
    pagerState: PagerState,
    modifier: Modifier = Modifier
) {
    HorizontalPager(
        modifier = modifier,
        state = pagerState
    ) { page ->
        TimelineColumn(
            timelineItems = state.homeScreenUiState.timelineItems,
            modifier = Modifier.fillMaxSize(),
            onIconClick = { id, username, host ->
                state.timelineEventSink(
                    TimelineItemEvent.OnTimelineItemIconClicked(
                        id,
                        username,
                        host
                    )
                )
            },
            onReplyClick = { id, user, host ->
                state.timelineEventSink(
                    TimelineItemEvent.OnTimelineItemReplyClicked(id, user, host)
                )
            },
            onRepostClick = { userId ->
                state.timelineEventSink(
                    TimelineItemEvent.OnTimelineItemRepostClicked(
                        userId
                    )
                )
            },
            onReactionClick = { userId ->
                state.timelineEventSink(
                    TimelineItemEvent.OnTimelineItemReactionClicked(
                        userId
                    )
                )
            },
            onOptionClick = { noteId, userId, host, username, text, uri ->
                state.timelineEventSink(
                    TimelineItemEvent.OnTimelineItemOptionClicked(
                        noteId,
                        userId,
                        host,
                        username,
                        text,
                        uri
                    )
                )
            }
        )
    }
}
