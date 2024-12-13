package me.sanao1006.feature.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingAppBarDefaults.ScreenOffset
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.zIndex
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuitx.effects.LaunchedImpressionEffect
import dagger.hilt.components.SingletonComponent
import ir.alirezaivaz.tablericons.TablerIcons
import kotlinx.coroutines.launch
import me.sanao1006.core.ui.MainScreenBottomAppBarWrapper
import me.sanao1006.core.ui.MainScreenDrawerWrapper
import me.sanao1006.core.ui.TimelineBottomSheet
import me.sanao1006.core.ui.TimelineColumn
import me.sanao1006.screens.HomeScreen
import me.sanao1006.screens.MainScreenType
import me.sanao1006.screens.event.GlobalIconEvent
import me.sanao1006.screens.event.NoteCreateEvent
import me.sanao1006.screens.event.TimelineItemEvent

@CircuitInject(HomeScreen::class, SingletonComponent::class)
@Composable
fun HomeScreenUi(state: HomeScreen.State, modifier: Modifier) {
    Box(modifier = modifier.fillMaxSize()) {
        val pagerState = rememberPagerState(initialPage = 1) { 3 }
        val scope = rememberCoroutineScope()
        val drawerState = rememberDrawerState(DrawerValue.Closed)
        val snackbarHostState = remember { SnackbarHostState() }
        LaunchedImpressionEffect(state.timelineUiState.isSuccessCreateNote) {
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
                snackbarHostState = { SnackbarHost(hostState = snackbarHostState) },
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
                        state.eventSink(HomeScreen.Event.TimelineEvent.OnLocalTimelineClicked)
                        pagerState.animateScrollToPage(0)
                    }
                },
                onSocialClick = {
                    scope.launch {
                        state.eventSink(HomeScreen.Event.TimelineEvent.OnSocialTimelineClicked)
                        pagerState.animateScrollToPage(1)
                    }
                },
                onGlobalClick = {
                    scope.launch {
                        state.eventSink(HomeScreen.Event.TimelineEvent.OnGlobalTimelineClicked)
                        pagerState.animateScrollToPage(2)
                    }
                },
                floatingActionButton = {
                    FloatingActionButton(
                        modifier = Modifier,
                        onClick = { state.noteCreateEventSink(NoteCreateEvent.OnNoteCreateClicked) }
                    ) {
                        Icon(painter = painterResource(TablerIcons.Pencil), "")
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
    snackbarHostState: @Composable () -> Unit,
    onGlobalIconClicked: () -> Unit,
    onHomeClick: () -> Unit,
    onSocialClick: () -> Unit,
    onGlobalClick: () -> Unit,
    floatingActionButton: @Composable () -> Unit
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
        snackbarHost = snackbarHostState
    ) {
        Box(
            contentAlignment = if (state.timelineUiState.timelineItems.isEmpty()) {
                Alignment.Center
            } else {
                Alignment.TopCenter
            },
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .pullRefresh(state = state.pullToRefreshState)
        ) {
            if (state.timelineUiState.timelineItems.isEmpty()) {
                CircularProgressIndicator()
            } else {
                PullRefreshIndicator(
                    refreshing = state.isRefreshed,
                    state = state.pullToRefreshState,
                    modifier = Modifier
                        .zIndex(1f)
                        .align(Alignment.TopCenter),
                    scale = true
                )
                HorizontalPager(
                    modifier = Modifier.zIndex(0f),
                    state = pagerState
                ) { page ->
                    TimelineColumn(
                        state = state,
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
                        onOptionClick = {
                            state.timelineEventSink(
                                TimelineItemEvent.OnTimelineItemOptionClicked(it)
                            )
                        }
                    )
                }
                TimelineBottomSheet(
                    isShowBottomSheet = state.timelineUiState.showBottomSheet,
                    timelineItemAction = state.timelineUiState.timelineAction,
                    onDismissRequest = { state.eventSink(HomeScreen.Event.OnDismissRequest) },
                    onRenoteIconCLick = { },
                    onOptionIconCLick = { }
                )
                MainScreenBottomAppBarWrapper(
                    modifier = Modifier
                        .align(BottomCenter)
                        .offset(y = -(ScreenOffset)),
                    mainScreenType = MainScreenType.HOME,
                    event = state.bottomAppBarEventSInk,
                    floatingActionButton = { floatingActionButton() }
                )
            }
        }
    }
}
