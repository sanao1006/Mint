package me.sanao1006.core.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material3.ContainedLoadingIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingAppBarDefaults.ScreenOffset
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Modifier
import androidx.compose.ui.zIndex
import me.sanao1006.core.model.uistate.TimelineItemAction
import me.sanao1006.screens.FavoritesScreen
import me.sanao1006.screens.HomeScreen
import me.sanao1006.screens.MainScreenState
import me.sanao1006.screens.MainScreenType
import me.sanao1006.screens.NotificationScreen
import me.sanao1006.screens.SubScreenState
import me.sanao1006.screens.event.NoteCreateEvent
import me.sanao1006.screens.event.TimelineItemEvent

@OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalMaterialApi::class)
@Composable
fun MainScreenTimelineContentBox(
    state: MainScreenState,
    mainScreenType: MainScreenType,
    snackbarHostState: SnackbarHostState,
    pullRefreshState: PullRefreshState,
    isRefreshed: Boolean,
    contentLoadingState: Boolean?,
    isEmptyContent: Boolean,
    modifier: Modifier = Modifier,
    onFabClick: () -> Unit = { state.noteCreateEventSink(NoteCreateEvent.OnNoteCreateClicked) },
    onRenoteIconClick: (RenoteActionIcon) -> Unit = { event ->
        when (event) {
            RenoteActionIcon.Renote -> {
                state.timelineEventSink(
                    TimelineItemEvent.OnRenoteClicked(
                        when (state) {
                            is HomeScreen.State -> state.timelineUiState.selectedUserId ?: ""
                            is NotificationScreen.State ->
                                state.timelineUiState.selectedUserId
                                    ?: ""

                            else -> ""
                        }
                    )
                )
            }

            RenoteActionIcon.Quote -> {
                when (state) {
                    is HomeScreen.State -> state.timelineEventSink(
                        TimelineItemEvent.OnQuoteClicked(
                            state.timelineUiState.selectedUserId ?: ""
                        )
                    )

                    is NotificationScreen.State -> state.timelineEventSink(
                        TimelineItemEvent.OnQuoteClicked(
                            state.timelineUiState.selectedUserId ?: ""
                        )
                    )
                }
            }
        }
    },
    onOptionIconClick: (OptionActionIcon) -> Unit = { event ->
        when (event) {
            OptionActionIcon.Detail -> {
                when (state) {
                    is HomeScreen.State -> state.timelineEventSink(
                        TimelineItemEvent.OnDetailClicked(
                            state.timelineUiState.selectedUserId ?: "",
                            null,
                            null
                        )
                    )

                    is NotificationScreen.State -> state.timelineEventSink(
                        TimelineItemEvent.OnDetailClicked(
                            state.timelineUiState.selectedUserId ?: "",
                            null,
                            null
                        )
                    )
                }
            }

            OptionActionIcon.Copy -> {
                when (state) {
                    is HomeScreen.State -> state.timelineEventSink(
                        TimelineItemEvent.OnCopyClicked(
                            state.timelineUiState.selectedNoteText ?: ""
                        )
                    )

                    is NotificationScreen.State -> state.timelineEventSink(
                        TimelineItemEvent.OnCopyClicked(
                            state.timelineUiState.selectedNoteText ?: ""
                        )
                    )
                }
            }

            OptionActionIcon.CopyLink -> {
                when (state) {
                    is HomeScreen.State -> state.timelineEventSink(
                        TimelineItemEvent.OnCopyLinkClicked(
                            state.timelineUiState.selectedNoteLink ?: ""
                        )
                    )

                    is NotificationScreen.State -> state.timelineEventSink(
                        TimelineItemEvent.OnCopyLinkClicked(
                            state.timelineUiState.selectedNoteLink ?: ""
                        )
                    )
                }
            }

            OptionActionIcon.Share -> {
                when (state) {
                    is HomeScreen.State -> state.timelineEventSink(
                        TimelineItemEvent.OnShareClicked(
                            state.timelineUiState.selectedNoteLink ?: ""
                        )
                    )

                    is NotificationScreen.State -> state.timelineEventSink(
                        TimelineItemEvent.OnShareClicked(
                            state.timelineUiState.selectedNoteLink ?: ""
                        )
                    )
                }
            }

            OptionActionIcon.Favorite, OptionActionIcon.UnFavorite -> {
                when (state) {
                    is HomeScreen.State -> state.timelineEventSink(
                        TimelineItemEvent.OnFavoriteClicked(
                            state.timelineUiState.selectedUserId ?: "",
                            snackbarHostState
                        )
                    )

                    is NotificationScreen.State -> state.timelineEventSink(
                        TimelineItemEvent.OnFavoriteClicked(
                            state.timelineUiState.selectedUserId ?: "",
                            snackbarHostState
                        )
                    )
                }
            }
        }
    },
    onDismissRequest: () -> Unit = {
        when (state) {
            is HomeScreen.State -> state.eventSink(HomeScreen.Event.OnDismissRequest)
            is NotificationScreen.State -> state.eventSink(
                NotificationScreen.Event.OnDismissRequest
            )
        }
    },
    notificationScreenTimelineContent: @Composable () -> Unit
) {
    Box(
        contentAlignment = Alignment.TopCenter,
        modifier = modifier
            .pullRefresh(state = pullRefreshState)
    ) {
        PullRefreshIndicator(
            refreshing = isRefreshed,
            state = pullRefreshState,
            modifier = Modifier
                .zIndex(1f)
                .align(Alignment.TopCenter),
            scale = true
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            when (contentLoadingState) {
                null -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        ContainedLoadingIndicator(
                            indicatorColor = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                false -> {
                }

                true -> {
                    if (isEmptyContent) {
                        NoContentsPlaceHolder()
                    } else {
                        notificationScreenTimelineContent()
                    }
                }
            }
        }
        TimelineBottomSheet(
            isShowBottomSheet = when (state) {
                is HomeScreen.State -> {
                    state.timelineUiState.showBottomSheet
                }

                is NotificationScreen.State -> {
                    state.timelineUiState.showBottomSheet
                }

                else -> false
            },
            timelineItemAction = when (state) {
                is HomeScreen.State -> state.timelineUiState.timelineAction
                is NotificationScreen.State -> state.timelineUiState.timelineAction
                else -> TimelineItemAction.None
            },
            isFavorite = when (state) {
                is HomeScreen.State -> state.timelineUiState.isFavorite
                is NotificationScreen.State -> state.timelineUiState.isFavorite
                else -> false
            },
            onDismissRequest = onDismissRequest,
            onRenoteIconCLick = onRenoteIconClick,
            onOptionIconCLick = onOptionIconClick
        )
        MainScreenBottomAppBarWrapper(
            modifier = Modifier
                .align(BottomCenter)
                .offset(y = -(ScreenOffset)),
            mainScreenType = mainScreenType,
            event = {
                when (state) {
                    is HomeScreen.State -> state.bottomAppBarEventSink(it)
                    is NotificationScreen.State -> state.bottomAppBarEventSink(it)
                }
            },
            onFabClick = onFabClick
        )
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalMaterialApi::class)
@Composable
fun SubScreenTimelineContentBox(
    state: SubScreenState,
    snackbarHostState: SnackbarHostState,
    pullRefreshState: PullRefreshState,
    isRefreshed: Boolean,
    contentLoadingState: Boolean?,
    isEmptyContent: Boolean,
    modifier: Modifier = Modifier,
    onRenoteIconClick: (RenoteActionIcon) -> Unit = { event ->
        when (event) {
            RenoteActionIcon.Renote -> {
                state.timelineEventSink(
                    TimelineItemEvent.OnRenoteClicked(
                        when (state) {
                            is FavoritesScreen.State ->
                                state.timelineUiState.selectedUserId
                                    ?: ""

                            else -> ""
                        }
                    )
                )
            }

            RenoteActionIcon.Quote -> {
                when (state) {
                    is FavoritesScreen.State -> state.timelineEventSink(
                        TimelineItemEvent.OnQuoteClicked(
                            state.timelineUiState.selectedUserId ?: ""
                        )
                    )
                }
            }
        }
    },
    onOptionIconClick: (OptionActionIcon) -> Unit = { event ->
        when (event) {
            OptionActionIcon.Detail -> {
                when (state) {
                    is FavoritesScreen.State -> state.timelineEventSink(
                        TimelineItemEvent.OnDetailClicked(
                            state.timelineUiState.selectedUserId ?: "",
                            null,
                            null
                        )
                    )
                }
            }

            OptionActionIcon.Copy -> {
                when (state) {
                    is FavoritesScreen.State -> state.timelineEventSink(
                        TimelineItemEvent.OnCopyClicked(
                            state.timelineUiState.selectedNoteText ?: ""
                        )
                    )
                }
            }

            OptionActionIcon.CopyLink -> {
                when (state) {
                    is FavoritesScreen.State -> state.timelineEventSink(
                        TimelineItemEvent.OnCopyLinkClicked(
                            state.timelineUiState.selectedNoteLink ?: ""
                        )
                    )
                }
            }

            OptionActionIcon.Share -> {
                when (state) {
                    is FavoritesScreen.State -> state.timelineEventSink(
                        TimelineItemEvent.OnShareClicked(
                            state.timelineUiState.selectedNoteLink ?: ""
                        )
                    )
                }
            }

            OptionActionIcon.Favorite, OptionActionIcon.UnFavorite -> {
                when (state) {
                    is FavoritesScreen.State -> state.timelineEventSink(
                        TimelineItemEvent.OnFavoriteClicked(
                            state.timelineUiState.selectedUserId ?: "",
                            snackbarHostState
                        )
                    )
                }
            }
        }
    },
    onDismissRequest: () -> Unit = {
        when (state) {
            is FavoritesScreen.State -> state.eventSink(FavoritesScreen.Event.OnDismissRequest)
        }
    },
    timelineContent: @Composable () -> Unit
) {
    Box(
        contentAlignment = Alignment.TopCenter,
        modifier = modifier
            .pullRefresh(state = pullRefreshState)
    ) {
        PullRefreshIndicator(
            refreshing = isRefreshed,
            state = pullRefreshState,
            modifier = Modifier
                .zIndex(1f)
                .align(Alignment.TopCenter),
            scale = true
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            when (contentLoadingState) {
                null -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        ContainedLoadingIndicator(
                            indicatorColor = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                false -> {
                    NoContentsPlaceHolder()
                }

                true -> {
                    if (isEmptyContent) {
                        NoContentsPlaceHolder()
                    } else {
                        timelineContent()
                    }
                }
            }
        }
        TimelineBottomSheet(
            isShowBottomSheet = when (state) {
                is FavoritesScreen.State -> {
                    state.timelineUiState.showBottomSheet
                }

                else -> false
            },
            timelineItemAction = when (state) {
                is FavoritesScreen.State -> state.timelineUiState.timelineAction
                else -> TimelineItemAction.None
            },
            isFavorite = when (state) {
                is FavoritesScreen.State -> state.timelineUiState.isFavorite
                else -> false
            },
            onDismissRequest = onDismissRequest,
            onRenoteIconCLick = onRenoteIconClick,
            onOptionIconCLick = onOptionIconClick
        )
    }
}
