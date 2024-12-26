package me.sanao1006.screens.event

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.slack.circuit.foundation.rememberAnsweringNavigator
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.presenter.Presenter
import javax.inject.Inject
import kotlinx.coroutines.launch
import me.sanao1006.core.data.compositionLocal.LocalNavigator
import me.sanao1006.screens.NoteScreen
import me.snao1006.res_value.ResString

data class NoteCreateState(
    val isSuccessCreateNote: Boolean? = null,
    val setSuccessCreateNoteState: (Boolean) -> Unit,
    val eventSink: (NoteCreateEvent) -> Unit
) : CircuitUiState

class NoteCreatePresenter @Inject constructor() : Presenter<NoteCreateState> {
    @Composable
    override fun present(): NoteCreateState {
        var isSuccessCreateNote: Boolean? by remember { mutableStateOf(null) }
        val navigator = LocalNavigator.current
        val scope = rememberCoroutineScope()
        val context = LocalContext.current
        val resultNavigator = rememberAnsweringNavigator<NoteScreen.Result>(navigator) { result ->
            isSuccessCreateNote = result.success
        }

        return NoteCreateState(
            isSuccessCreateNote = isSuccessCreateNote,
            setSuccessCreateNoteState = { isSuccessCreateNote = it }
        ) { event ->
            when (event) {
                is NoteCreateEvent.OnNoteCreated -> {
                    isSuccessCreateNote?.let { flg ->
                        scope.launch {
                            event.snackbarHostState.showSnackbar(
                                message = if (flg) {
                                    context.getString(ResString.post_result_message_success)
                                } else {
                                    context.getString(ResString.post_result_message_failed)
                                }
                            )
                        }
                    }
                }

                is NoteCreateEvent.OnNoteCreateClicked -> {
                    resultNavigator.goTo(NoteScreen())
                }
            }
        }
    }
}
