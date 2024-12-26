package me.sanao1006.screens.event.notecreate

import android.content.Context
import androidx.compose.material3.SnackbarHostState
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.GoToNavigator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import me.sanao1006.screens.NoteScreen
import me.snao1006.res_value.ResString

sealed class NoteCreateEvent : CircuitUiEvent {
    data class OnNoteCreated(
        val snackbarHostState: SnackbarHostState,
        val scope: CoroutineScope
    ) : NoteCreateEvent()

    data object OnNoteCreateClicked : NoteCreateEvent()
}

fun NoteCreateEvent.handleNoteCreateEvent(
    isSuccessCreateNote: Boolean?,
    context: Context,
    nav: GoToNavigator
) {
    when (this) {
        is NoteCreateEvent.OnNoteCreated -> {
            isSuccessCreateNote?.let { flg ->
                this.scope.launch {
                    this@handleNoteCreateEvent.snackbarHostState.showSnackbar(
                        message = if (flg) {
                            context.getString(ResString.post_result_message_success)
                        } else {
                            context.getString(ResString.post_result_message_failed)
                        }
                    )
                }
            }
        }

        NoteCreateEvent.OnNoteCreateClicked -> {
            nav.goTo(NoteScreen())
        }
    }
}
