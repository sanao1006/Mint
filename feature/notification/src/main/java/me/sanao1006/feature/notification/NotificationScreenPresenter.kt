package me.sanao1006.feature.notification

import android.content.Intent
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.foundation.rememberAnsweringNavigator
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuitx.effects.LaunchedImpressionEffect
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.sanao1006.core.domain.home.CreateNotesUseCase
import me.sanao1006.core.domain.home.UpdateAccountUseCase
import me.sanao1006.core.domain.notification.GetNotificationsUseCase
import me.sanao1006.core.model.LoginUserInfo
import me.sanao1006.core.model.notes.Visibility
import me.sanao1006.core.model.uistate.NotificationUiState
import me.sanao1006.core.model.uistate.TimelineItemAction
import me.sanao1006.screens.NoteScreen
import me.sanao1006.screens.NotificationScreen
import me.sanao1006.screens.event.TimelineItemEvent
import me.sanao1006.screens.event.handleBottomAppBarActionEvent
import me.sanao1006.screens.event.handleDrawerEvent
import me.sanao1006.screens.event.handleNavigationIconClicked
import me.sanao1006.screens.event.handleNoteCreateEvent
import me.sanao1006.screens.event.handleTimelineItemIconClicked
import me.sanao1006.screens.event.handleTimelineItemReplyClicked

class NotificationScreenPresenter @AssistedInject constructor(
    @Assisted private val navigator: Navigator,
    private val updateMyAccountUseCase: UpdateAccountUseCase,
    private val getNotificationsUseCase: GetNotificationsUseCase,
    private val createNotesUseCase: CreateNotesUseCase
) : Presenter<NotificationScreen.State> {
    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    override fun present(): NotificationScreen.State {
        val clipboardManager = LocalClipboardManager.current
        var isSuccessCreateNote: Boolean? by rememberRetained { mutableStateOf(null) }
        var loginUserInfo: LoginUserInfo by rememberRetained {
            mutableStateOf(
                LoginUserInfo()
            )
        }

        val context = LocalContext.current
        val scope = rememberCoroutineScope()
        val nav = rememberAnsweringNavigator<NoteScreen.Result>(navigator) { result ->
            isSuccessCreateNote = result.success
        }

        var notificationUiState: NotificationUiState by rememberRetained {
            mutableStateOf(NotificationUiState())
        }

        var isRefreshed by remember { mutableStateOf(false) }
        val pullRefreshState = rememberPullRefreshState(
            refreshing = isRefreshed,
            onRefresh = {
                scope.launch {
                    isRefreshed = true
                    notificationUiState = getNotificationsUseCase()
                    delay(1500L)
                    isRefreshed = false
                }
            },
            refreshThreshold = 50.dp,
            refreshingOffset = 50.dp
        )

        LaunchedImpressionEffect(Unit) {
            loginUserInfo = updateMyAccountUseCase()
            notificationUiState = getNotificationsUseCase()
        }
        return NotificationScreen.State(
            notificationUiState = notificationUiState,
            isSuccessCreateNote = isSuccessCreateNote,
            navigator = navigator,
            drawerUserInfo = loginUserInfo,
            pullToRefreshState = pullRefreshState,
            isRefreshed = isRefreshed,
            timelineEventSink = { event ->
                when (event) {
                    is TimelineItemEvent.OnTimelineItemIconClicked ->
                        event.handleTimelineItemIconClicked(navigator)

                    is TimelineItemEvent.OnTimelineItemReplyClicked ->
                        event.handleTimelineItemReplyClicked(navigator)

                    is TimelineItemEvent.OnTimelineItemRepostClicked -> {
                        notificationUiState =
                            notificationUiState.copy(
                                showBottomSheet = true,
                                timelineAction = TimelineItemAction.Renote,
                                selectedUserId = event.id
                            )
                    }

                    is TimelineItemEvent.OnTimelineItemReactionClicked -> {}

                    is TimelineItemEvent.OnTimelineItemOptionClicked -> {
                        notificationUiState = notificationUiState.copy(
                            showBottomSheet = true,
                            timelineAction = TimelineItemAction.Option,
                            selectedUserId = event.userId
                        )
                    }

                    is TimelineItemEvent.OnRenoteClicked -> {
                        scope.launch {
                            createNotesUseCase.invoke(
                                text = null,
                                visibility = Visibility.PUBLIC,
                                localOnly = false,
                                reactionAcceptance = null,
                                renoteId = event.id
                            )
                            notificationUiState = notificationUiState.copy(showBottomSheet = false)
                        }
                    }

                    is TimelineItemEvent.OnQuoteClicked -> {
                        notificationUiState = notificationUiState.copy(showBottomSheet = false)
                        nav.goTo(NoteScreen(idForQuote = event.id))
                    }

                    is TimelineItemEvent.OnDetailClicked -> {
                        notificationUiState = notificationUiState.copy(showBottomSheet = false)
                    }

                    is TimelineItemEvent.OnCopyClicked -> {
                        notificationUiState = notificationUiState.copy(showBottomSheet = false)
                        clipboardManager.setText(AnnotatedString(event.text))
                    }

                    is TimelineItemEvent.OnCopyLinkClicked -> {
                        notificationUiState = notificationUiState.copy(showBottomSheet = false)
                        clipboardManager.setText(AnnotatedString(event.link))
                    }

                    is TimelineItemEvent.OnShareClicked -> {
                        notificationUiState = notificationUiState.copy(showBottomSheet = false)
                        val sendIntent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TEXT, event.link)
                            type = "text/plain"
                        }
                        Intent.createChooser(sendIntent, null).also { context.startActivity(it) }
                    }

                    is TimelineItemEvent.OnFavoriteClicked -> {
                        notificationUiState = notificationUiState.copy(showBottomSheet = false)
                    }
                }
            },
            noteCreateEventSink = { event ->
                event.handleNoteCreateEvent(
                    isSuccessCreateNote,
                    context,
                    navigator
                )
            },
            drawerEventSink = { event -> event.handleDrawerEvent(navigator, loginUserInfo) },
            globalIconEventSink = { event -> event.handleNavigationIconClicked(navigator) },
            bottomAppBarEventSInk = { event -> event.handleBottomAppBarActionEvent(navigator) },
            eventSink = { event ->
                when (event) {
                    NotificationScreen.Event.OnDismissRequest -> {
                        notificationUiState = notificationUiState.copy(showBottomSheet = false)
                    }
                }
            }
        )
    }
}

@AssistedFactory
@CircuitInject(NotificationScreen::class, SingletonComponent::class)
interface Factory {
    fun create(navigator: Navigator): NotificationScreenPresenter
}
