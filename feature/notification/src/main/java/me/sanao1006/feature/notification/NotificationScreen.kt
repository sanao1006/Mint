package me.sanao1006.feature.notification

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuitx.effects.LaunchedImpressionEffect
import dagger.hilt.components.SingletonComponent
import ir.alirezaivaz.tablericons.TablerIcons
import me.sanao1006.core.ui.MainScreenDrawerWrapper
import me.sanao1006.core.ui.TimelineContentBox
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
    val context = LocalContext.current
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
                context = context,
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
                        containerColor = MaterialTheme.colorScheme.primary,
                        onClick = { state.noteCreateEventSink(NoteCreateEvent.OnNoteCreateClicked) }
                    ) {
                        Icon(painter = painterResource(TablerIcons.Pencil), "")
                    }
                }
            )
        }
    }
}

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalMaterial3ExpressiveApi::class,
    ExperimentalMaterialApi::class
)
@Composable
private fun NotificationScreenContent(
    state: NotificationScreen.State,
    context: Context,
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
        TimelineContentBox(
            state = state,
            modifier = Modifier.padding(it),
            mainScreenType = MainScreenType.NOTIFICATION,
            pullRefreshState = state.pullToRefreshState,
            isRefreshed = state.isRefreshed,
            contentLoadingState = state.notificationUiState.isSuccessLoading,
            isEmptyContent = state.notificationUiState.notificationUiStateObjects.isEmpty(),
            floatingActionButton = floatingActionButton
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
