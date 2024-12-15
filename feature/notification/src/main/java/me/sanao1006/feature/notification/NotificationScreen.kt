package me.sanao1006.feature.notification

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ContainedLoadingIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingAppBarDefaults.ScreenOffset
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuitx.effects.LaunchedImpressionEffect
import dagger.hilt.components.SingletonComponent
import ir.alirezaivaz.tablericons.TablerIcons
import me.sanao1006.core.ui.MainScreenBottomAppBarWrapper
import me.sanao1006.core.ui.MainScreenDrawerWrapper
import me.sanao1006.core.ui.NotificationColumn
import me.sanao1006.core.ui.OptionActionIcon
import me.sanao1006.core.ui.RenoteActionIcon
import me.sanao1006.core.ui.TimelineBottomSheet
import me.sanao1006.screens.MainScreenType
import me.sanao1006.screens.NotificationScreen
import me.sanao1006.screens.event.GlobalIconEvent
import me.sanao1006.screens.event.NoteCreateEvent
import me.sanao1006.screens.event.TimelineItemEvent
import me.snao1006.res_value.ResString

@CircuitInject(NotificationScreen::class, SingletonComponent::class)
@Composable
fun NotificationScreenUi(state: NotificationScreen.State, modifier: Modifier) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedImpressionEffect(state.isSuccessCreateNote) {
        state.noteCreateEventSink(
            NoteCreateEvent.OnNoteCreated(
                snackbarHostState = snackbarHostState,
                scope = scope
            )
        )
    }
    Box(modifier = modifier.fillMaxSize()) {
        MainScreenDrawerWrapper(
            loginUserInfo = state.drawerUserInfo,
            drawerState = drawerState,
            scope = scope,
            event = state.drawerEventSink
        ) {
            NotificationScreenContent(
                state = state,
                modifier = Modifier,
                snackbarHost = { SnackbarHost(snackbarHostState) },
                onGlobalIconClicked = {
                    state.globalIconEventSink(
                        GlobalIconEvent.OnGlobalIconClicked(
                            drawerState,
                            scope
                        )
                    )
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

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun NotificationScreenContent(
    state: NotificationScreen.State,
    modifier: Modifier = Modifier,
    snackbarHost: @Composable () -> Unit,
    onGlobalIconClicked: () -> Unit,
    floatingActionButton: @Composable () -> Unit
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(ResString.title_notification),
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onGlobalIconClicked) {
                        Icon(painter = painterResource(TablerIcons.Menu2), "")
                    }
                }
            )
        },
        snackbarHost = snackbarHost
    ) {
        Box(
            contentAlignment = Alignment.TopCenter,
            modifier = Modifier.padding(it)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                when (state.notificationUiState.isSuccessLoading) {
                    null -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            ContainedLoadingIndicator()
                        }
                    }

                    false -> {
                    }

                    true -> {
                        NotificationColumn(
                            modifier = Modifier.fillMaxSize(),
                            notifications = state.notificationUiState.notificationUiStateObjects,
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
                            onOptionClick = { noteId, userId, host, username, uri ->
                                state.timelineEventSink(
                                    TimelineItemEvent.OnTimelineItemOptionClicked(
                                        noteId,
                                        userId,
                                        host,
                                        username,
                                        uri
                                    )
                                )
                            }
                        )
                    }
                }
            }
            TimelineBottomSheet(
                isShowBottomSheet = state.notificationUiState.showBottomSheet,
                timelineItemAction = state.notificationUiState.timelineAction,
                onDismissRequest = { state.eventSink(NotificationScreen.Event.OnDismissRequest) },
                onRenoteIconCLick = { event ->
                    when (event) {
                        RenoteActionIcon.Renote -> {
                            state.timelineEventSink(
                                TimelineItemEvent.OnRenoteClicked(
                                    state.notificationUiState.selectedUserId ?: ""
                                )
                            )
                        }

                        RenoteActionIcon.Quote -> {
                            state.timelineEventSink(
                                TimelineItemEvent.OnQuoteClicked(
                                    state.notificationUiState.selectedUserId ?: ""
                                )
                            )
                        }
                    }
                },
                onOptionIconCLick = { event ->
                    when (event) {
                        OptionActionIcon.Detail -> {
                            state.timelineEventSink(
                                TimelineItemEvent.OnDetailClicked(
                                    state.notificationUiState.selectedUserId ?: "",
                                    null,
                                    null
                                )
                            )
                        }

                        OptionActionIcon.Copy -> {
                            state.timelineEventSink(
                                TimelineItemEvent.OnCopyClicked(
                                    state.notificationUiState.selectedUserId ?: ""
                                )
                            )
                        }

                        OptionActionIcon.CopyLink -> {
                            state.timelineEventSink(
                                TimelineItemEvent.OnCopyLinkClicked(
                                    state.notificationUiState.selectedUserId ?: ""
                                )
                            )
                        }

                        OptionActionIcon.Share -> {
                            state.timelineEventSink(
                                TimelineItemEvent.OnShareClicked(
                                    state.notificationUiState.selectedUserId ?: ""
                                )
                            )
                        }

                        OptionActionIcon.Favorite -> {
                            state.timelineEventSink(
                                TimelineItemEvent.OnFavoriteClicked(
                                    state.notificationUiState.selectedUserId ?: ""
                                )
                            )
                        }
                    }
                }
            )
            MainScreenBottomAppBarWrapper(
                modifier = Modifier
                    .align(BottomCenter)
                    .offset(y = -(ScreenOffset)),
                mainScreenType = MainScreenType.NOTIFICATION,
                event = state.bottomAppBarEventSInk,
                floatingActionButton = { floatingActionButton() }
            )
        }
    }
}
