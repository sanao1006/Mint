package me.sanao1006.screens.event.bottomAppBar

import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.Navigator
import me.sanao1006.screens.HomeScreen
import me.sanao1006.screens.NotificationScreen

sealed class BottomAppBarActionEvent : CircuitUiEvent {
    data object OnHomeIconClicked : BottomAppBarActionEvent()
    data object OnNotificationIconClicked : BottomAppBarActionEvent()
}

fun BottomAppBarActionEvent.handleBottomAppBarActionEvent(
    navigator: Navigator
) {
    when (this) {
        BottomAppBarActionEvent.OnHomeIconClicked -> {
            navigator.goTo(HomeScreen)
        }

        BottomAppBarActionEvent.OnNotificationIconClicked -> {
            navigator.goTo(NotificationScreen)
        }
    }
}
