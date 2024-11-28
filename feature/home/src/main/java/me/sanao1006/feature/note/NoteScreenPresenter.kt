package me.sanao1006.feature.note

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.presenter.Presenter
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject

@CircuitInject(NoteScreen::class, SingletonComponent::class)
class NoteScreenPresenter @Inject constructor() : Presenter<NoteScreen.State> {
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

                else -> {}
            }
        }
    }
}

data class NoteScreenUiState(
    val noteText: String
)