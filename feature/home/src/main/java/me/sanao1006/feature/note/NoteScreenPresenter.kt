package me.sanao1006.feature.note

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.launch
import me.sanao1006.core.model.home.notes.ReactionAcceptance
import me.sanao1006.core.model.home.notes.Visibility
import me.sanao1006.feature.note.domain.CreateNotesUseCase

class NoteScreenPresenter @AssistedInject constructor(
    @Assisted private val navigator: Navigator,
    private val createNotesUseCase: CreateNotesUseCase
) : Presenter<NoteScreen.State> {
    @Composable
    override fun present(): NoteScreen.State {
        var uiState by rememberRetained { mutableStateOf(NoteScreenUiState("")) }
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
                        createNotesUseCase(
                            text = uiState.noteText,
                            visibility = uiState.visibility,
                            reactionAcceptance = uiState.reactionAcceptance
                        )
                    }
                    navigator.pop()
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

data class NoteScreenUiState(
    val noteText: String,
    val visibility: Visibility = Visibility.PUBLIC,
    val localOnly: Boolean = false,
    val reactionAcceptance: ReactionAcceptance? = null,
    val isShowBottomSheet: Boolean = false,
    val noteOptionContent: NoteOptionContent = NoteOptionContent.VISIBILITY
)

@CircuitInject(NoteScreen::class, SingletonComponent::class)
@AssistedFactory
fun interface NoteScreenFactory {
    fun create(navigator: Navigator): NoteScreenPresenter
}
