package me.sanao1006.screens

import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen
import kotlinx.coroutines.CoroutineScope
import kotlinx.parcelize.Parcelize
import me.sanao1006.core.model.home.notes.NoteOptionContent
import me.sanao1006.core.model.home.notes.NoteScreenUiState
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
