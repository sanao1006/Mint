package me.sanao1006.feature.home

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
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuitx.effects.LaunchedImpressionEffect
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.sanao1006.core.data.compositionLocal.LocalNavigator
import me.sanao1006.core.domain.favorites.CreateFavoritesUseCase
import me.sanao1006.core.domain.favorites.DeleteFavoritesUseCase
import me.sanao1006.core.domain.favorites.GetNoteStateUseCase
import me.sanao1006.core.domain.home.CreateNotesUseCase
import me.sanao1006.core.domain.home.GetNotesTimelineUseCase
import me.sanao1006.core.domain.home.TimelineType
import me.sanao1006.core.domain.home.UpdateAccountUseCase
import me.sanao1006.core.model.LoginUserInfo
import me.sanao1006.core.model.notes.TimelineItem
import me.sanao1006.core.model.notes.Visibility
import me.sanao1006.core.model.uistate.HomeScreenUiState
import me.sanao1006.core.model.uistate.TimelineItemAction
import me.sanao1006.screens.HomeScreen
import me.sanao1006.screens.NoteScreen
import me.sanao1006.screens.event.BottomAppBarPresenter
import me.sanao1006.screens.event.TimelineItemEvent
import me.sanao1006.screens.event.favorite
import me.sanao1006.screens.event.handleDrawerEvent
import me.sanao1006.screens.event.handleNavigationIconClicked
import me.sanao1006.screens.event.handleNoteCreateEvent
import me.sanao1006.screens.event.handleTimelineItemIconClicked
import me.sanao1006.screens.event.handleTimelineItemReplyClicked
import javax.inject.Inject

@CircuitInject(HomeScreen::class, SingletonComponent::class)
class HomeScreenPresenter @Inject constructor(
    private val getNotesTimelineUseCase: GetNotesTimelineUseCase,
    private val updateMyAccountUseCase: UpdateAccountUseCase,
    private val createNotesUseCase: CreateNotesUseCase,
    private val createFavoritesUseCase: CreateFavoritesUseCase,
    private val deleteFavoritesUseCase: DeleteFavoritesUseCase,
    private val getNoteStateUseCase: GetNoteStateUseCase,
    private val bottomAppBarPresenter: BottomAppBarPresenter
) : Presenter<HomeScreen.State> {

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    override fun present(): HomeScreen.State {
        val navigator = LocalNavigator.current
        val bottomAppBarPresenter = bottomAppBarPresenter.present()
        val clipBoardManager = LocalClipboardManager.current
        var isSuccessCreateNote: Boolean? by rememberRetained { mutableStateOf(null) }
        var loginUserInfo: LoginUserInfo by rememberRetained {
            mutableStateOf(
                LoginUserInfo()
            )
        }
        val context = LocalContext.current
        val scope = rememberCoroutineScope()
        val resultNavigator = rememberAnsweringNavigator<NoteScreen.Result>(navigator) { result ->
            isSuccessCreateNote = result.success
        }

        var timelineType by rememberRetained { mutableStateOf(TimelineType.SOCIAL) }
        var homeScreenUiState: HomeScreenUiState by rememberRetained(timelineType) {
            mutableStateOf(HomeScreenUiState())
        }

        var isRefreshed by remember { mutableStateOf(false) }
        val pullRefreshState = rememberPullRefreshState(
            refreshing = isRefreshed,
            onRefresh = {
                scope.launch {
                    isRefreshed = true
                    val timelineItems: List<TimelineItem> = getNotesTimelineUseCase(timelineType)
                    homeScreenUiState = if (timelineItems.isEmpty()) {
                        homeScreenUiState.copy(
                            timelineItems = emptyList(),
                            isSuccessLoading = false
                        )
                    } else {
                        homeScreenUiState.copy(
                            timelineItems = timelineItems,
                            isSuccessLoading = true
                        )
                    }
                    delay(1000L)
                    isRefreshed = false
                }
            },
            refreshThreshold = 50.dp,
            refreshingOffset = 50.dp
        )
        LaunchedImpressionEffect {
            val timelineItems: List<TimelineItem> = getNotesTimelineUseCase(timelineType)
            homeScreenUiState = if (timelineItems.isEmpty()) {
                homeScreenUiState.copy(
                    timelineItems = emptyList(),
                    isSuccessLoading = false
                )
            } else {
                homeScreenUiState.copy(
                    timelineItems = timelineItems,
                    isSuccessLoading = true
                )
            }
            loginUserInfo = updateMyAccountUseCase()
        }

        LaunchedImpressionEffect(timelineType) {
            val timelineItems: List<TimelineItem> = getNotesTimelineUseCase(timelineType)
            homeScreenUiState = if (timelineItems.isEmpty()) {
                homeScreenUiState.copy(
                    timelineItems = emptyList(),
                    isSuccessLoading = false
                )
            } else {
                homeScreenUiState.copy(
                    timelineItems = timelineItems,
                    isSuccessLoading = true
                )
            }
        }

        return HomeScreen.State(
            homeScreenUiState = homeScreenUiState,
            navigator = navigator,
            pullToRefreshState = pullRefreshState,
            isRefreshed = isRefreshed,
            drawerUserInfo = loginUserInfo,
            noteCreateEventSink = { event ->
                event.handleNoteCreateEvent(
                    isSuccessCreateNote,
                    context,
                    resultNavigator
                )
            },
            timelineEventSink = { event ->
                when (event) {
                    is TimelineItemEvent.OnTimelineItemIconClicked ->
                        event.handleTimelineItemIconClicked(navigator)

                    is TimelineItemEvent.OnTimelineItemReplyClicked ->
                        event.handleTimelineItemReplyClicked(navigator)

                    is TimelineItemEvent.OnTimelineItemRepostClicked -> {
                        homeScreenUiState =
                            homeScreenUiState.copy(
                                showBottomSheet = true,
                                timelineAction = TimelineItemAction.Renote,
                                selectedUserId = event.id
                            )
                    }

                    is TimelineItemEvent.OnTimelineItemReactionClicked -> {}

                    is TimelineItemEvent.OnTimelineItemOptionClicked -> {
                        scope.launch {
                            getNoteStateUseCase.invoke(event.id)
                                .onSuccess {
                                    homeScreenUiState =
                                        homeScreenUiState.copy(
                                            showBottomSheet = true,
                                            timelineAction = TimelineItemAction.Option,
                                            isFavorite = it.isFavorited,
                                            selectedUserId = event.id,
                                            selectedNoteText = event.text,
                                            selectedNoteLink = event.uri
                                        )
                                }
                                .onFailure {
                                    homeScreenUiState =
                                        homeScreenUiState.copy(
                                            showBottomSheet = true,
                                            timelineAction = TimelineItemAction.Option,
                                            isFavorite = false,
                                            selectedUserId = event.id,
                                            selectedNoteText = event.text,
                                            selectedNoteLink = event.uri
                                        )
                                }
                        }
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
                            homeScreenUiState = homeScreenUiState.copy(showBottomSheet = false)
                        }
                    }

                    is TimelineItemEvent.OnQuoteClicked -> {
                        homeScreenUiState = homeScreenUiState.copy(showBottomSheet = false)
                        resultNavigator.goTo(NoteScreen(idForQuote = event.id))
                    }

                    is TimelineItemEvent.OnDetailClicked -> {
                        homeScreenUiState = homeScreenUiState.copy(showBottomSheet = false)
                    }

                    is TimelineItemEvent.OnCopyClicked -> {
                        homeScreenUiState = homeScreenUiState.copy(
                            showBottomSheet = false
                        )
                        clipBoardManager.setText(AnnotatedString(event.text))
                    }

                    is TimelineItemEvent.OnCopyLinkClicked -> {
                        homeScreenUiState = homeScreenUiState.copy(showBottomSheet = false)
                        clipBoardManager.setText(AnnotatedString(event.link))
                    }

                    is TimelineItemEvent.OnShareClicked -> {
                        homeScreenUiState = homeScreenUiState.copy(showBottomSheet = false)
                        val sendIntent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TEXT, event.link)
                            type = "text/plain"
                        }
                        Intent.createChooser(sendIntent, null).also { context.startActivity(it) }
                    }

                    is TimelineItemEvent.OnFavoriteClicked -> {
                        homeScreenUiState = homeScreenUiState.copy(showBottomSheet = false)
                        scope.launch {
                            favorite(
                                isFavorite = homeScreenUiState.isFavorite,
                                snackbarHostState = event.snackbarHostState,
                                context = context,
                                notFavoriteCallBack = {
                                    deleteFavoritesUseCase.invoke(event.noteId)
                                },
                                isFavoriteCallBack = { createFavoritesUseCase.invoke(event.noteId) }
                            )
                        }
                    }
                }
            },
            drawerEventSink = { event -> event.handleDrawerEvent(navigator, loginUserInfo) },
            bottomAppBarEventSink = bottomAppBarPresenter.eventSink,
            globalIconEventSink = { event -> event.handleNavigationIconClicked(navigator) }
        ) { event ->
            when (event) {
                HomeScreen.Event.TimelineEvent.OnLocalTimelineClicked ->
                    timelineType = TimelineType.HOME

                HomeScreen.Event.TimelineEvent.OnSocialTimelineClicked
                    -> timelineType = TimelineType.SOCIAL

                HomeScreen.Event.TimelineEvent.OnGlobalTimelineClicked ->
                    timelineType = TimelineType.GLOBAL

                HomeScreen.Event.OnDismissRequest -> {
                    homeScreenUiState = homeScreenUiState.copy(showBottomSheet = false)
                }
            }
        }
    }
}
