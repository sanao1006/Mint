package me.sanao1006.feature.notification

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.BottomAppBarScrollBehavior
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuitx.effects.LaunchedImpressionEffect
import dagger.hilt.components.SingletonComponent
import ir.alirezaivaz.tablericons.TablerIcons
import me.sanao1006.core.ui.MainScreenBottomAppBarWrapper
import me.sanao1006.core.ui.MainScreenDrawerWrapper
import me.sanao1006.core.ui.MainScreenTimelineContentBox
import me.sanao1006.screens.MainScreenType
import me.sanao1006.screens.NotificationScreen
import me.sanao1006.screens.event.globalIcon.GlobalIconEvent
import me.sanao1006.screens.event.notecreate.NoteCreateEvent
import me.sanao1006.screens.event.timeline.TimelineItemEvent
import me.snao1006.res_value.ResString

@OptIn(ExperimentalMaterial3Api::class)
@CircuitInject(NotificationScreen::class, SingletonComponent::class)
@Composable
fun NotificationScreenUi(state: NotificationScreen.State, modifier: Modifier) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val exitAlwaysScrollBehavior =
        BottomAppBarDefaults.exitAlwaysScrollBehavior()

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
            expandDialog = state.expandDialog,
            scope = scope,
            event = state.drawerEventSink
        ) {
            NotificationScreenContent(
                state = state,
                scrollBehavior = exitAlwaysScrollBehavior,
                context = context,
                modifier = Modifier,
                snackbarHostState = snackbarHostState,
                onGlobalIconClicked = {
                    state.globalIconEventSink(
                        GlobalIconEvent.OnGlobalIconClicked(
                            drawerState,
                            scope
                        )
                    )
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NotificationScreenContent(
    state: NotificationScreen.State,
    scrollBehavior: BottomAppBarScrollBehavior,
    context: Context,
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState,
    onGlobalIconClicked: () -> Unit
) {
    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
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
        bottomBar = {
            MainScreenBottomAppBarWrapper(
                scrollBehavior = scrollBehavior,
                mainScreenType = MainScreenType.NOTIFICATION,
                event = { state.bottomAppBarEventSink(it) },
                onFabClick = { state.noteCreateEventSink(NoteCreateEvent.OnNoteCreateClicked) }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) {
        MainScreenTimelineContentBox(
            state = state,
            snackbarHostState = snackbarHostState,
            isRefreshed = state.isRefreshed,
            contentLoadingState = state.timelineUiState.isSuccessLoading,
            modifier = Modifier.padding(it),
            isEmptyContent = state.notificationUiState.notificationUiStateObjects.isEmpty()
        ) {
            NotificationColumn(
                modifier = Modifier.fillMaxSize(),
                context = context,
                notifications = state
                    .notificationUiState
                    .notificationUiStateObjects,
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
                        TimelineItemEvent.OnTimelineItemReplyClicked(id, user, userId, text, host)
                    )
                },
                onRepostClick = { noteId, userId, text ->
                    state.timelineEventSink(
                        TimelineItemEvent.OnTimelineItemRepostClicked(
                            id = noteId,
                            userId = userId,
                            text = text
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
