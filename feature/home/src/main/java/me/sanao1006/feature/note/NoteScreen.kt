package me.sanao1006.feature.note

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen
import dagger.hilt.components.SingletonComponent
import kotlinx.parcelize.Parcelize
import me.sanao1006.core.designsystem.MintTheme

@Parcelize
data object NoteScreen : Screen {
    data class State(
        val uiState: NoteScreenUiState,
        val eventSink: (Event) -> Unit
    ) : CircuitUiState

    sealed class Event : CircuitUiState {
        data class OnNoteTextChanged(val text: String) : Event()
    }
}

@CircuitInject(NoteScreen::class, SingletonComponent::class)
@Composable
fun NoteScreenUi(state: NoteScreen.State, modifier: Modifier) {
    MintTheme {
        Box(modifier = modifier.fillMaxSize()) {
            TextField(
                value = state.uiState.noteText,
                onValueChange = { state.eventSink(NoteScreen.Event.OnNoteTextChanged(it)) },
                placeholder = {
                    Text(
                        text = "Note",
                        style = MaterialTheme.typography.headlineSmall
                    )
                },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}
