package me.sanao1006.screens.event.bottomAppBar

import androidx.compose.foundation.lazy.LazyListState
import com.slack.circuit.runtime.CircuitUiEvent

sealed class BottomAppBarActionEvent : CircuitUiEvent {
    data class OnHomeIconClicked(
        val lazyListState: LazyListState
    ) : BottomAppBarActionEvent()

    data class OnNotificationIconClicked(
        val lazyListState: LazyListState
    ) : BottomAppBarActionEvent()
}
