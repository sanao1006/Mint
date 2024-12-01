package me.sanao1006.feature.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.slack.circuit.codegen.annotations.CircuitInject
import dagger.hilt.components.SingletonComponent
import ir.alirezaivaz.tablericons.TablerIcons
import kotlinx.coroutines.launch
import me.sanao1006.core.ui.MainScreenBottomSheet
import me.sanao1006.screens.HomeScreen
import me.sanao1006.screens.MainScreenType

@CircuitInject(HomeScreen::class, SingletonComponent::class)
@Composable
fun HomeScreenUi(state: HomeScreen.State, modifier: Modifier) {
    Box(modifier = modifier.fillMaxSize()) {
        val pagerState = rememberPagerState(initialPage = 1) { 3 }
        val scope = rememberCoroutineScope()
        val drawerState = rememberDrawerState(DrawerValue.Closed)
        val snackbarHostState = remember { SnackbarHostState() }
        LaunchedEffect(state.isSuccessCreateNote) {
            state.eventSink(HomeScreen.Event.OnNoteCreated(snackbarHostState, scope))
        }

        HomeScreenDrawer(
            drawerState = drawerState
        ) {
            HomeScreenUiContent(
                state = state,
                pagerState = pagerState,
                snackbarHostState = { SnackbarHost(hostState = snackbarHostState) },
                onNavigationIconClick = {
                    scope.launch {
                        drawerState.apply {
                            if (isClosed) open() else close()
                        }
                    }
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
                    FloatingActionButton(onClick = { state.eventSink(HomeScreen.Event.OnNoteCreateClicked) }) {
                        Icon(painter = painterResource(TablerIcons.Plus), "")
                    }
                }
            )
        }
    }
}

@Composable
private fun HomeScreenUiContent(
    state: HomeScreen.State,
    pagerState: PagerState,
    snackbarHostState: @Composable () -> Unit,
    onNavigationIconClick: () -> Unit,
    onHomeClick: () -> Unit,
    onSocialClick: () -> Unit,
    onGlobalClick: () -> Unit,
    floatingActionButton: @Composable () -> Unit
) {
    Scaffold(
        topBar = {
            HomeScreenTopAppBar(
                onNavigationIconClick = onNavigationIconClick,
                onHomeClick = onHomeClick,
                onSocialClick = onSocialClick,
                onGlobalClick = onGlobalClick
            )
        },
        bottomBar = {
            MainScreenBottomSheet(mainSheetType = MainScreenType.HOME)
        },
        snackbarHost = snackbarHostState,
        floatingActionButton = floatingActionButton
    ) {
        Column(modifier = Modifier.padding(it)) {
            HorizontalPager(
                state = pagerState
            ) { page ->
                TimelineColumn(
                    state = state,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}
