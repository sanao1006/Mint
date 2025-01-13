package me.sanao1006.screens.event.timeline

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.presenter.Presenter
import javax.inject.Inject
import kotlinx.coroutines.launch
import me.sanao1006.core.data.compositionLocal.LocalNavigator
import me.sanao1006.core.domain.favorites.CreateFavoritesUseCase
import me.sanao1006.core.domain.favorites.DeleteFavoritesUseCase
import me.sanao1006.core.domain.favorites.GetNoteStateUseCase
import me.sanao1006.core.domain.home.CreateNotesUseCase
import me.sanao1006.core.model.notes.Visibility
import me.sanao1006.core.model.uistate.TimelineItemAction
import me.sanao1006.core.model.uistate.TimelineUiState
import me.sanao1006.datastore.DataStoreRepository
import me.sanao1006.screens.NoteScreen
import me.sanao1006.screens.NoteTargetObject

data class TimelineState(
    var uiState: TimelineUiState,
    val setSuccessLoading: (Boolean) -> Unit,
    val setShowBottomSheet: (Boolean) -> Unit,
    val eventSink: (TimelineItemEvent) -> Unit
) : CircuitUiState

class TimelineEventPresenter @Inject constructor(
    private val getNoteStateUseCase: GetNoteStateUseCase,
    private val createNotesUseCase: CreateNotesUseCase,
    private val createFavoritesUseCase: CreateFavoritesUseCase,
    private val deleteFavoritesUseCase: DeleteFavoritesUseCase,
    private val dataStoreRepository: DataStoreRepository
) : Presenter<TimelineState> {
    @Composable
    override fun present(): TimelineState {
        var uiState: TimelineUiState by rememberRetained { mutableStateOf(TimelineUiState()) }
        val scope = rememberCoroutineScope()
        val navigator = LocalNavigator.current
        val clipBoardManager = LocalClipboardManager.current
        val context = LocalContext.current
        return TimelineState(
            uiState = uiState,
            setSuccessLoading = { uiState = uiState.copy(isSuccessLoading = it) },
            setShowBottomSheet = { uiState = uiState.copy(showBottomSheet = it) }
        ) { event ->
            when (event) {
                is TimelineItemEvent.OnTimelineItemIconClicked ->
                    event.handleTimelineItemIconClicked(navigator)

                is TimelineItemEvent.OnTimelineItemReplyClicked ->
                    event.handleTimelineItemReplyClicked(navigator)

                is TimelineItemEvent.OnTimelineItemRepostClicked -> {
                    uiState =
                        uiState.copy(
                            showBottomSheet = true,
                            timelineAction = TimelineItemAction.Renote,
                            selectedNoteId = event.id,
                            selectedNoteUserId = event.userId,
                            selectedNoteText = event.text
                        )
                }

                is TimelineItemEvent.OnTimelineItemReactionClicked -> {}

                is TimelineItemEvent.OnTimelineItemOptionClicked -> {
                    scope.launch {
                        uiState = uiState.copy(
                            showBottomSheet = true,
                            timelineAction = TimelineItemAction.Option,
                            selectedNoteId = event.id,
                            selectedNoteText = event.text,
                            selectedNoteLink = event.uri
                        )
                        getNoteStateUseCase.invoke(event.id)
                            .onSuccess { uiState = uiState.copy(isFavorite = it.isFavorited) }
                            .onFailure { uiState = uiState.copy(isFavorite = false) }
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
                        uiState = uiState.copy(showBottomSheet = false)
                    }
                }

                is TimelineItemEvent.OnQuoteClicked -> {
                    uiState = uiState.copy(showBottomSheet = false)
                    navigator.goTo(
                        NoteScreen(
                            quoteObject = NoteTargetObject(
                                id = event.id,
                                userId = event.userId,
                                text = event.text,
                                user = ""
                            )
                        )
                    )
                }

                is TimelineItemEvent.OnDetailClicked -> {
                    uiState = uiState.copy(showBottomSheet = false)
                }

                is TimelineItemEvent.OnCopyClicked -> {
                    uiState = uiState.copy(
                        showBottomSheet = false
                    )
                    clipBoardManager.setText(AnnotatedString(event.text))
                }

                is TimelineItemEvent.OnCopyLinkClicked -> {
                    scope.launch {
                        uiState = uiState.copy(showBottomSheet = false)
                        val url = if (event.link.isEmpty()) {
                            val base = dataStoreRepository.getBaseUrl()
                            "$base/notes/${event.id}"
                        } else {
                            event.link
                        }
                        clipBoardManager.setText(AnnotatedString(url))
                    }
                }

                is TimelineItemEvent.OnShareClicked -> {
                    scope.launch {
                        uiState = uiState.copy(showBottomSheet = false)
                        val url = if (event.link.isEmpty()) {
                            val base = dataStoreRepository.getBaseUrl()
                            "$base/notes/${event.id}"
                        } else {
                            event.link
                        }
                        val sendIntent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TEXT, url)
                            type = "text/plain"
                        }
                        Intent.createChooser(sendIntent, null)
                            .also { context.startActivity(it) }
                    }
                }

                is TimelineItemEvent.OnFavoriteClicked -> {
                    uiState = uiState.copy(showBottomSheet = false)
                    scope.launch {
                        favorite(
                            isFavorite = uiState.isFavorite,
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
        }

        fun setSuccessLoading(isSuccessLoading: Boolean) {
            uiState = uiState.copy(isSuccessLoading = isSuccessLoading)
        }
    }
}
