package me.sanao1006.feature.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.zIndex
import com.slack.circuit.codegen.annotations.CircuitInject
import dagger.hilt.components.SingletonComponent
import ir.alirezaivaz.tablericons.TablerIcons
import kotlinx.coroutines.launch
import me.sanao1006.core.ui.MainScreenBottomAppBar
import me.sanao1006.core.ui.TimelineColumn
import me.sanao1006.screens.HomeScreen
import me.sanao1006.screens.MainScreenType

@OptIn(ExperimentalMaterial3Api::class)
@CircuitInject(HomeScreen::class, SingletonComponent::class)
@Composable
fun HomeScreenUi(state: HomeScreen.State, modifier: Modifier) {
    Box(modifier = modifier.fillMaxSize()) {
        val pagerState = rememberPagerState(initialPage = 1) { 3 }
        val scope = rememberCoroutineScope()
        val drawerState = rememberDrawerState(DrawerValue.Closed)
        val snackbarHostState = remember { SnackbarHostState() }
        val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
        LaunchedEffect(state.isSuccessCreateNote) {
            state.eventSink(HomeScreen.Event.OnNoteCreated(snackbarHostState, scope))
        }

        HomeScreenDrawer(
            loginUserInfo = state.drawerUserInfo,
            drawerState = drawerState,
            onDrawerFavoriteClick = {
                state.eventSink(HomeScreen.Event.OnDrawerFavoriteClicked)
            },
            onDrawerAnnouncementClick = {
                state.eventSink(HomeScreen.Event.OnDrawerAnnouncementClicked)
            },
            onDrawerClipClick = {
                state.eventSink(HomeScreen.Event.OnDrawerClipClicked)
            },
            onDrawerAntennaClick = {
                state.eventSink(HomeScreen.Event.OnDrawerAntennaClicked)
            },
            onDrawerExploreClick = {
                state.eventSink(HomeScreen.Event.OnDrawerExploreClicked)
            },
            onDrawerChannelClick = {
                state.eventSink(HomeScreen.Event.OnDrawerChannelClicked)
            },
            onDrawerDriveClick = {
                state.eventSink(HomeScreen.Event.OnDrawerDriveClicked)
            },
            onDrawerAboutClick = {
                state.eventSink(HomeScreen.Event.OnDrawerAboutClicked)
            },
            onDrawerAccountPreferencesClick = {
                state.eventSink(HomeScreen.Event.OnDrawerAccountPreferencesClicked)
            },
            onDrawerSettingsClick = {
                state.eventSink(HomeScreen.Event.OnDrawerSettingsClicked)
            },
            onIconClick = {
                state.eventSink(HomeScreen.Event.OnDrawerIconClicked)
            },
            onFollowingCountClick = {
                state.eventSink(HomeScreen.Event.OnDrawerFollowingCountClicked)
            },
            onFollowersCountClick = {
                state.eventSink(HomeScreen.Event.OnDrawerFollowersCountClicked)
            }
        ) {
            HomeScreenUiContent(
                state = state,
                pagerState = pagerState,
                modifier = Modifier
                    .nestedScroll(scrollBehavior.nestedScrollConnection),
                scrollBehavior = scrollBehavior,
                snackbarHostState = { SnackbarHost(hostState = snackbarHostState) },
                onNavigationIconClick = {
                    state.eventSink(HomeScreen.Event.OnNavigationIconClicked(drawerState, scope))
                },
                onHomeClick = {
                    scope.launch {
                        state.eventSink(HomeScreen.Event.OnLocalTimelineClicked)
                        pagerState.animateScrollToPage(0)
                    }
                },
                onSocialClick = {
                    scope.launch {
                        state.eventSink(HomeScreen.Event.OnSocialTimelineClicked)
                        pagerState.animateScrollToPage(1)
                    }
                },
                onGlobalClick = {
                    scope.launch {
                        state.eventSink(HomeScreen.Event.OnGlobalTimelineClicked)
                        pagerState.animateScrollToPage(2)
                    }
                },
                floatingActionButton = {
                    FloatingActionButton(
                        modifier = Modifier,
                        onClick = { state.eventSink(HomeScreen.Event.OnNoteCreateClicked) }
                    ) {
                        Icon(painter = painterResource(TablerIcons.Pencil), "")
                    }
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
private fun HomeScreenUiContent(
    state: HomeScreen.State,
    pagerState: PagerState,
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior,
    snackbarHostState: @Composable () -> Unit,
    onNavigationIconClick: () -> Unit,
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
                scrollBehavior = scrollBehavior,
                onNavigationIconClick = onNavigationIconClick,
                onHomeClick = onHomeClick,
                onSocialClick = onSocialClick,
                onGlobalClick = onGlobalClick
            )
        },
        bottomBar = {
            MainScreenBottomAppBar(
                mainSheetType = MainScreenType.HOME,
                onHomeClick = { state.eventSink(HomeScreen.Event.OnHomeIconClicked) },
                onSearchClick = { state.eventSink(HomeScreen.Event.OnSearchIconClicked) },
                onNotificationClick = {
                    state.eventSink(HomeScreen.Event.OnNotificationIconClicked)
                },
                floatingActionButton = floatingActionButton
            )
        },
        snackbarHost = snackbarHostState
    ) {
        Box(
            contentAlignment = Alignment.TopCenter,
            modifier = Modifier
                .padding(it)
                .pullRefresh(state = state.pullToRefreshState)
        ) {
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
                        state.eventSink(
                            HomeScreen.Event.OnTimelineIconClicked(
                                id,
                                username,
                                host
                            )
                        )
                    }
                )
            }
        }
    }
}
