package me.sanao1006.feature.favorites

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
import me.sanao1006.core.domain.favorites.GetMyFavoriteUseCase
import me.sanao1006.core.domain.home.CreateNotesUseCase
import me.sanao1006.core.model.notes.Visibility
import me.sanao1006.core.model.uistate.FavoritesScreenUiState
import me.sanao1006.core.model.uistate.TimelineItemAction
import me.sanao1006.screens.FavoritesScreen
import me.sanao1006.screens.NoteScreen
import me.sanao1006.screens.event.TimelineItemEvent
import me.sanao1006.screens.event.handleNavigationIconClicked
import me.sanao1006.screens.event.handleTimelineItemIconClicked
import me.sanao1006.screens.event.handleTimelineItemReplyClicked

class FavoritesScreenPresenter @AssistedInject constructor(
    @Assisted private val navigator: Navigator,
    private val createNotesUseCase: CreateNotesUseCase,
    private val getMyFavoriteUseCase: GetMyFavoriteUseCase
) : Presenter<FavoritesScreen.State> {
    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    override fun present(): FavoritesScreen.State {
        val clipBoardManager = LocalClipboardManager.current
        var isSuccessCreateNote: Boolean? by rememberRetained { mutableStateOf(null) }
        val nav = rememberAnsweringNavigator<NoteScreen.Result>(navigator) { result ->
            isSuccessCreateNote = result.success
        }
        var isRefreshed by remember { mutableStateOf(false) }
        var favoritesScreenUiState by rememberRetained {
            mutableStateOf(FavoritesScreenUiState())
        }
        val context = LocalContext.current
        val scope = rememberCoroutineScope()

        val pullRefreshState = rememberPullRefreshState(
            refreshing = isRefreshed,
            onRefresh = {
                scope.launch {
                    isRefreshed = true
                    val favorites = getMyFavoriteUseCase.invoke()
                    favoritesScreenUiState = favorites
                    delay(1000L)
                    isRefreshed = false
                }
            },
            refreshThreshold = 50.dp,
            refreshingOffset = 50.dp
        )

        LaunchedImpressionEffect(Unit) {
            val favorites = getMyFavoriteUseCase.invoke()
            favoritesScreenUiState = favorites
        }

        return FavoritesScreen.State(
            navigator = navigator,
            favoritesScreenUiState = favoritesScreenUiState,
            pullToRefreshState = pullRefreshState,
            timelineEventSink = { event ->
                when (event) {
                    is TimelineItemEvent.OnTimelineItemIconClicked ->
                        event.handleTimelineItemIconClicked(navigator)

                    is TimelineItemEvent.OnTimelineItemReplyClicked ->
                        event.handleTimelineItemReplyClicked(navigator)

                    is TimelineItemEvent.OnTimelineItemRepostClicked -> {
                        favoritesScreenUiState =
                            favoritesScreenUiState.copy(
                                showBottomSheet = true,
                                timelineAction = TimelineItemAction.Renote,
                                selectedUserId = event.id
                            )
                    }

                    is TimelineItemEvent.OnTimelineItemReactionClicked -> {}

                    is TimelineItemEvent.OnTimelineItemOptionClicked -> {
                        favoritesScreenUiState =
                            favoritesScreenUiState.copy(
                                showBottomSheet = true,
                                timelineAction = TimelineItemAction.Option,
                                selectedUserId = event.id,
                                selectedNoteText = event.text,
                                selectedNoteLink = event.uri
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
                            favoritesScreenUiState =
                                favoritesScreenUiState.copy(showBottomSheet = false)
                        }
                    }

                    is TimelineItemEvent.OnQuoteClicked -> {
                        favoritesScreenUiState =
                            favoritesScreenUiState.copy(showBottomSheet = false)
                        nav.goTo(NoteScreen(idForQuote = event.id))
                    }

                    is TimelineItemEvent.OnDetailClicked -> {
                        favoritesScreenUiState =
                            favoritesScreenUiState.copy(showBottomSheet = false)
                    }

                    is TimelineItemEvent.OnCopyClicked -> {
                        favoritesScreenUiState = favoritesScreenUiState.copy(
                            showBottomSheet = false
                        )
                        clipBoardManager.setText(AnnotatedString(event.text))
                    }

                    is TimelineItemEvent.OnCopyLinkClicked -> {
                        favoritesScreenUiState =
                            favoritesScreenUiState.copy(showBottomSheet = false)
                        clipBoardManager.setText(AnnotatedString(event.link))
                    }

                    is TimelineItemEvent.OnShareClicked -> {
                        favoritesScreenUiState =
                            favoritesScreenUiState.copy(showBottomSheet = false)
                        val sendIntent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TEXT, event.link)
                            type = "text/plain"
                        }
                        Intent.createChooser(sendIntent, null).also { context.startActivity(it) }
                    }

                    is TimelineItemEvent.OnFavoriteClicked -> {
                        favoritesScreenUiState =
                            favoritesScreenUiState.copy(showBottomSheet = false)
                    }
                }
            },
            globalIconEventSink = { event -> event.handleNavigationIconClicked(navigator) }
        ) { event ->
            when (event) {
                FavoritesScreen.Event.OnDismissRequest -> {
                    favoritesScreenUiState = favoritesScreenUiState.copy(showBottomSheet = false)
                }
            }
        }
    }
}

@AssistedFactory
@CircuitInject(FavoritesScreen::class, SingletonComponent::class)
interface Factory {
    fun create(navigator: Navigator): FavoritesScreenPresenter
}
