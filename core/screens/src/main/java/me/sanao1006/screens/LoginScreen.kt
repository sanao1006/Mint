package me.sanao1006.screens

import android.content.Context
import androidx.compose.runtime.Immutable
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen
import kotlinx.coroutines.CoroutineScope
import kotlinx.parcelize.Parcelize

@Parcelize
data object LoginScreen : Screen {
    @Immutable
    data class State(
        val domain: String = "",
        val error: String = "",
        val authState: AuthStateType = AuthStateType.FIXED,
        val eventSink: (Event) -> Unit
    ) : CircuitUiState

    sealed class Event : CircuitUiEvent {
        data class OnTextChanged(val text: String) : Event()
        data class OnButtonClicked(val scope: CoroutineScope, val context: Context) : Event()
        data class OnAuthButtonClicked(val scope: CoroutineScope) : Event()
    }
}

enum class AuthStateType { FIXED, WAITING, SUCCESS }
