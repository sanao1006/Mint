package me.sanao1006.feature.note

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.parcelize.Parcelize
import me.sanao1006.core.designsystem.MintTheme
import me.sanao1006.core.model.home.notes.ReactionAcceptance
import me.sanao1006.core.model.home.notes.Visibility

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
        data class OnShowBottomSheet(val optionContent: NoteOptionContent) : Event()
        data object OnHideBottomSheet : Event()
        data class OnVisibilityChanged(val visibility: Visibility) : Event()
        data class OnLocalOnlyChanged(val localOnly: Boolean) : Event()
        data class OnReactionAcceptanceChanged(val reactionAcceptance: ReactionAcceptance?) :
            Event()
    }
}

enum class NoteOptionContent {
    VISIBILITY,
    LOCAL_ONLY,
    REACTION_ACCEPTANCE
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
        Box(
            modifier = modifier
                .fillMaxSize()
                .safeDrawingPadding()
        ) {
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
                NoteScreenContent(
                    modifier = Modifier
                        .padding(it)
                        .focusRequester(focusRequester),
                    state = state,
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NoteScreenContent(
    state: NoteScreen.State,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        NoteScreenTextField(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            text = state.uiState.noteText,
            onValueChange = { note ->
                state.eventSink(
                    NoteScreen.Event.OnNoteTextChanged(
                        note
                    )
                )
            }
        )
        NoteOptionRow(
            isShowBottomSheet = state.uiState.isShowBottomSheet,
            noteOptionContent = state.uiState.noteOptionContent,
            modifier = Modifier.imePadding(),
            onBottomSheetOuterClicked = { state.eventSink(NoteScreen.Event.OnHideBottomSheet) },
            onIconClicked = { state.eventSink(NoteScreen.Event.OnShowBottomSheet(it)) },
            onVisibilityClicked = { state.eventSink(NoteScreen.Event.OnVisibilityChanged(it)) },
            onLocalOnlyClicked = { state.eventSink(NoteScreen.Event.OnLocalOnlyChanged(it)) },
            onReactionAcceptanceClicked = {
                state.eventSink(
                    NoteScreen.Event.OnReactionAcceptanceChanged(
                        it
                    )
                )
            }
        )
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

@OptIn(ExperimentalMaterial3Api::class)
@PreviewLightDark
@Composable
fun A() {
    MintTheme {
        NoteOptionRow(
            true,
            NoteOptionContent.VISIBILITY,
            Modifier,
            {},
            {},
            {},
            {},
            {}
        )
    }
}
