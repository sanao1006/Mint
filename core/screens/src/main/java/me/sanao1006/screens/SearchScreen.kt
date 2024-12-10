package me.sanao1006.screens

import androidx.compose.material3.DrawerState
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen
import kotlinx.coroutines.CoroutineScope
import kotlinx.parcelize.Parcelize

@Parcelize
data object SearchScreen : Screen {
    data class State(
        val query: String = "",
        val eventSink: (Event) -> Unit
    ) : CircuitUiState

    sealed class Event : CircuitUiEvent {
        data class OnNavigationIconClicked(
            val drawerState: DrawerState,
            val scope: CoroutineScope
        ) : Event()
    }
}
