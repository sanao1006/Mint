package me.sanao1006.feature.favorites

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.slack.circuit.codegen.annotations.CircuitInject
import dagger.hilt.components.SingletonComponent
import me.sanao1006.core.ui.DrawerItem
import me.sanao1006.core.ui.DrawerItemScreenWrapper
import me.sanao1006.core.ui.SubScreenTimelineContentBox
import me.sanao1006.core.ui.TimelineColumn
import me.sanao1006.screens.FavoritesScreen
import me.sanao1006.screens.event.timeline.TimelineItemEvent

@OptIn(ExperimentalMaterialApi::class)
@CircuitInject(FavoritesScreen::class, SingletonComponent::class)
@Composable
fun FavoritesScreen(state: FavoritesScreen.State, modifier: Modifier) {
    Box(modifier = modifier.fillMaxSize()) {
        val snackbarHostState = remember { SnackbarHostState() }
        DrawerItemScreenWrapper(
            drawerItem = DrawerItem.FAVORITE,
            snackbarHostState = snackbarHostState,
            globalIconEventSink = state.globalIconEventSink
        ) {
            SubScreenTimelineContentBox(
                modifier = it,
                state = state,
                snackbarHostState = snackbarHostState,
                pullRefreshState = state.pullToRefreshState,
                isRefreshed = state.isRefreshed,
                contentLoadingState = state.timelineUiState.isSuccessLoading,
                isEmptyContent = state.favoritesScreenUiState.timelineItems.isEmpty()
            ) {
                TimelineColumn(
                    timelineItems = state.favoritesScreenUiState.timelineItems,
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
    }
}
