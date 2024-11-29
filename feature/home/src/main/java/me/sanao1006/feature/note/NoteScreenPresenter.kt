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

class NoteScreenPresenter @AssistedInject constructor(
    @Assisted private val navigator: Navigator
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
                    // Post note

                    navigator.pop()
                }

                else -> {}
            }
        }
    }
}

data class NoteScreenUiState(
    val noteText: String
)

@CircuitInject(NoteScreen::class, SingletonComponent::class)
@AssistedFactory
fun interface NoteScreenFactory {
    fun create(navigator: Navigator): NoteScreenPresenter
}
