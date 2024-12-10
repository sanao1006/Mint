package me.sanao1006.screens

import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen
import kotlinx.parcelize.Parcelize
import me.sanao1006.core.model.LoginUserInfo
import me.sanao1006.screens.event.DrawerEvent
import me.sanao1006.screens.event.GlobalIconEvent

@Parcelize
data object SearchScreen : Screen {
    data class State(
        val loginUserInfo: LoginUserInfo,
        val drawerEventSink: (DrawerEvent) -> Unit,
        val globalIconEventSink: (GlobalIconEvent) -> Unit,
        val eventSink: (Event) -> Unit
    ) : CircuitUiState

    sealed class Event : CircuitUiEvent
}
