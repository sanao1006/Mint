package me.sanao1006.screens

import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.PopResult
import com.slack.circuit.runtime.screen.Screen
import kotlinx.coroutines.CoroutineScope
import kotlinx.parcelize.Parcelize

@Parcelize
data object NoteScreen : Screen {
    data class State(
        val uiState: me.sanao1006.core.model.notes.NoteScreenUiState,
        val eventSink: (Event) -> Unit
    ) : CircuitUiState

    sealed class Event : CircuitUiState {
        data object OnBackClicked : Event()
        data class OnNoteTextChanged(val text: String) : Event()
        data class OnNotePostClicked(val scope: CoroutineScope) : Event()
        data class OnShowBottomSheet(val optionContent: me.sanao1006.core.model.notes.NoteOptionContent) :
            Event()

        data object OnHideBottomSheet : Event()
        data class OnVisibilityChanged(val visibility: me.sanao1006.core.model.notes.Visibility) :
            Event()

        data class OnLocalOnlyChanged(val localOnly: Boolean) : Event()
        data class OnReactionAcceptanceChanged(val reactionAcceptance: me.sanao1006.core.model.notes.ReactionAcceptance?) :
            Event()
    }

    @Parcelize
    data class Result(val success: Boolean) : PopResult
}
