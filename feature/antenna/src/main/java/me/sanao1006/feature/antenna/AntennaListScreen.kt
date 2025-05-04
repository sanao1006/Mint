package me.sanao1006.feature.antenna

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
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
import me.sanao1006.screens.AntennaListScreen
import me.sanao1006.screens.event.globalIcon.GlobalIconEvent
import me.sanao1006.screens.event.timeline.TimelineItemEvent

@OptIn(ExperimentalMaterial3Api::class)
@CircuitInject(AntennaListScreen::class, SingletonComponent::class)
@Composable
fun AntennaListScreen(state: AntennaListScreen.State, modifier: Modifier) {
    Box(modifier = modifier.fillMaxSize()) {
        val snackbarHostState = remember { SnackbarHostState() }
        DrawerItemScreenWrapper(
            drawerItem = DrawerItem.ANTENNA,
            snackbarHostState = snackbarHostState,
            onBackIconClick = {
                state.globalIconEventSink(GlobalIconEvent.OnBackBeforeScreen)
            }
        ) {
            SubScreenTimelineContentBox(
                state = state,
                snackbarHostState = snackbarHostState,
                modifier = it,
                isRefreshed = state.isRefreshed,
                contentLoadingState = state.timelineUiState.isSuccessLoading,
                isEmptyContent = state.uiState.timelineItems.isEmpty()
            ) {
                TimelineColumn(
                    timelineItems = state.uiState.timelineItems,
                    onIconClick = { id, username, host ->
                        state.timelineEventSink(
                            TimelineItemEvent.OnTimelineItemIconClicked(
                                id,
                                username,
                                host
                            )
                        )
                    },
                    onReplyClick = { id, user, userId, text, host ->
                        state.timelineEventSink(
                            TimelineItemEvent.OnTimelineItemReplyClicked(
                                id,
                                user,
                                userId,
                                text,
                                host
                            )
                        )
                    },
                    onRepostClick = { noteId, userId, text ->
                        state.timelineEventSink(
                            TimelineItemEvent.OnTimelineItemRepostClicked(
                                noteId,
                                userId,
                                text
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
