package me.sanao1006.feature.note

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.presenter.Presenter
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.launch
import me.sanao1006.core.data.compositionLocal.LocalNavigator
import me.sanao1006.core.data.util.suspendRunCatching
import me.sanao1006.core.domain.home.CreateNotesUseCase
import me.sanao1006.core.model.uistate.NoteScreenUiState
import me.sanao1006.screens.NoteScreen

class NoteScreenPresenter @AssistedInject constructor(
    @Assisted private val screen: NoteScreen,
    private val createNotesUseCase: CreateNotesUseCase
) : Presenter<NoteScreen.State> {
    @Composable
    override fun present(): NoteScreen.State {
        val navigator = LocalNavigator.current
        var uiState by rememberRetained {
            mutableStateOf(
                NoteScreenUiState(
                    noteText = screen.replyObject?.user ?: "",
                    replyId = screen.replyObject?.id,
                    renoteId = screen.idForQuote
                )
            )
        }
        return NoteScreen.State(
            uiState = uiState
        ) {
            when (it) {
                is NoteScreen.Event.OnNoteTextChanged -> {
                    uiState = uiState.copy(noteText = it.text)
                }

                is NoteScreen.Event.OnBackClicked -> {
                    navigator.pop()
                }

                is NoteScreen.Event.OnNotePostClicked -> {
                    it.scope.launch {
                        suspendRunCatching {
                            if (!uiState.replyId.isNullOrEmpty()) {
                                createNotesUseCase(
                                    text = uiState.noteText,
                                    visibility = uiState.visibility,
                                    localOnly = uiState.localOnly,
                                    reactionAcceptance = uiState.reactionAcceptance,
                                    replyId = uiState.replyId
                                )
                            } else if (!uiState.renoteId.isNullOrEmpty()) {
                                createNotesUseCase(
                                    text = uiState.noteText,
                                    visibility = uiState.visibility,
                                    localOnly = uiState.localOnly,
                                    reactionAcceptance = null,
                                    renoteId = uiState.renoteId
                                )
                            } else {
                                createNotesUseCase(
                                    text = uiState.noteText,
                                    visibility = uiState.visibility,
                                    localOnly = uiState.localOnly,
                                    reactionAcceptance = uiState.reactionAcceptance
                                )
                            }
                        }.onSuccess {
                            navigator.pop(result = NoteScreen.Result(true))
                        }.onFailure {
                            navigator.pop(result = NoteScreen.Result(false))
                        }
                    }
                }

                is NoteScreen.Event.OnVisibilityChanged -> {
                    uiState = uiState.copy(
                        isShowBottomSheet = false,
                        visibility = it.visibility
                    )
                }

                is NoteScreen.Event.OnLocalOnlyChanged -> {
                    uiState = uiState.copy(
                        isShowBottomSheet = false,
                        localOnly = it.localOnly
                    )
                }

                is NoteScreen.Event.OnReactionAcceptanceChanged -> {
                    uiState = uiState.copy(
                        isShowBottomSheet = false,
                        reactionAcceptance = it.reactionAcceptance
                    )
                }

                is NoteScreen.Event.OnShowBottomSheet -> {
                    uiState = uiState.copy(
                        isShowBottomSheet = true,
                        noteOptionContent = it.optionContent
                    )
                }

                is NoteScreen.Event.OnHideBottomSheet -> {
                    uiState = uiState.copy(isShowBottomSheet = false)
                }

                else -> {}
            }
        }
    }
}

@CircuitInject(NoteScreen::class, SingletonComponent::class)
@AssistedFactory
fun interface Factory {
    fun create(screen: NoteScreen): NoteScreenPresenter
}
