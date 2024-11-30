package me.sanao1006.feature.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.slack.circuit.codegen.annotations.CircuitInject
import dagger.hilt.components.SingletonComponent
import ir.alirezaivaz.tablericons.TablerIcons
import kotlinx.coroutines.launch
import me.sanao1006.screens.HomeScreen

@OptIn(ExperimentalMaterial3Api::class)
@CircuitInject(HomeScreen::class, SingletonComponent::class)
@Composable
fun HomeScreenUi(state: HomeScreen.State, modifier: Modifier) {
    Box(modifier = modifier.fillMaxSize()) {
        val pagerState = rememberPagerState(initialPage = 1) { 3 }
        val scope = rememberCoroutineScope()
        Scaffold(
            topBar = {
                HomeScreenTopAppBar(
                    onNavigationIconClick = {},
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
                    }
                )
            },
            floatingActionButton = {
                FloatingActionButton(onClick = { state.eventSink(HomeScreen.Event.OnNoteCreateClicked) }) {
                    Icon(painter = painterResource(TablerIcons.Plus), "")
                }
            }
        ) { it ->
            Column(modifier = Modifier.padding(it)) {
                HorizontalPager(
                    state = pagerState
                ) { page ->
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        items(state.uiState) {
                            it?.let { timelineUiState ->
                                TimeLineItem(
                                    modifier = Modifier.padding(bottom = 8.dp),
                                    timelineUiState = timelineUiState
                                )
                                HorizontalDivider()
                            }
                        }
                    }
                }
            }
        }
    }
}
