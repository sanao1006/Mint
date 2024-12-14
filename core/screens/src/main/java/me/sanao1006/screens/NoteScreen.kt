package me.sanao1006.screens

import android.os.Parcelable
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.PopResult
import com.slack.circuit.runtime.screen.Screen
import kotlinx.coroutines.CoroutineScope
import kotlinx.parcelize.Parcelize
import me.sanao1006.core.model.uistate.NoteOptionContent
import me.sanao1006.core.model.uistate.NoteScreenUiState

@Parcelize
data class NoteScreen(
    val replyObject: ReplyObject? = null
) : Screen {
    data class State(
        val uiState: NoteScreenUiState,
        val eventSink: (Event) -> Unit
    ) : CircuitUiState

    sealed class Event : CircuitUiState {
        data object OnBackClicked : Event()
        data class OnNoteTextChanged(val text: String) : Event()
        data class OnNotePostClicked(val scope: CoroutineScope) : Event()
        data class OnShowBottomSheet(
            val optionContent: NoteOptionContent
        ) :
            Event()

        data object OnHideBottomSheet : Event()
        data class OnVisibilityChanged(val visibility: me.sanao1006.core.model.notes.Visibility) :
            Event()

        data class OnLocalOnlyChanged(val localOnly: Boolean) : Event()
        data class OnReactionAcceptanceChanged(
            val reactionAcceptance: me.sanao1006.core.model.notes.ReactionAcceptance?
        ) :
            Event()
    }

    @Parcelize
    data class Result(val success: Boolean) : PopResult
}

@Parcelize
data class ReplyObject(
    val id: String,
    val user: String
) : Parcelable
