package me.sanao1006.feature.note

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.parcelize.Parcelize
import me.sanao1006.core.designsystem.MintTheme

@Parcelize
data object NoteScreen : Screen {
    data class State(
        val uiState: NoteScreenUiState,
        val eventSink: (Event) -> Unit
    ) : CircuitUiState

    sealed class Event : CircuitUiState {
        data object OnBackClicked : Event()
        data class OnNoteTextChanged(val text: String) : Event()
        data class OnNotePostClicked(val scope: CoroutineScope) : Event()
    }
}

@CircuitInject(NoteScreen::class, SingletonComponent::class)
@Composable
fun NoteScreenUi(state: NoteScreen.State, modifier: Modifier) {
    MintTheme {
        val scope = rememberCoroutineScope()
        val focusRequester = rememberRetained { FocusRequester() }
        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }
        Box(modifier = modifier.fillMaxSize()) {
            Scaffold(
                topBar = {
                    NoteScreenTopAppBar(
                        onBackClicked = { state.eventSink(NoteScreen.Event.OnBackClicked) },
                        onNotePostClicked = {
                            state.eventSink(
                                NoteScreen.Event.OnNotePostClicked(
                                    scope
                                )
                            )
                        }
                    )
                }
            ) {
                NoteScreenTextField(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(it)
                        .focusRequester(focusRequester),
                    text = state.uiState.noteText,
                    onValueChange = { note ->
                        state.eventSink(
                            NoteScreen.Event.OnNoteTextChanged(
                                note
                            )
                        )
                    }
                )
            }
        }
    }
}

@Composable
private fun NoteScreenTextField(
    modifier: Modifier = Modifier,
    text: String,
    onValueChange: (String) -> Unit
) {
    TextField(
        modifier = modifier,
        value = text,
        onValueChange = onValueChange,
        placeholder = {
            Text(
                text = "Note",
                style = MaterialTheme.typography.headlineSmall
            )
        }
    )
}
