package me.sanao1006.screens

import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen
import kotlinx.parcelize.Parcelize
import me.sanao1006.core.model.uistate.ChannelListUiState
import me.sanao1006.screens.event.globalIcon.GlobalIconEvent

@Parcelize
data object ChannelListScreen : Screen {
    data class State(
        val channelListUiState: ChannelListUiState,
        val globalIconEventSink: (GlobalIconEvent) -> Unit,
        val eventSink: (Event) -> Unit
    ) : CircuitUiState

    sealed class Event : CircuitUiEvent {
        data class OnChannelClick(val id: String) : Event()
        data object OnEnterClick : Event()
        data class OnChannelNameChange(val channelName: String) : Event()

        data object OnSearchClick : Event()
        data object OnTrendClick : Event()
        data object OnFavoriteClick : Event()
        data object OnFollowTabClick : Event()
        data class OnPageChange(val page: Int) : Event()
    }
}
