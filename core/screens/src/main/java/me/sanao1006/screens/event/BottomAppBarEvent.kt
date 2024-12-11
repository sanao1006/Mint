package me.sanao1006.screens.event

import com.slack.circuit.runtime.Navigator
import me.sanao1006.screens.HomeScreen

sealed class BottomAppBarActionEvent {
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
            // TODO goto Notification Screen
        }
    }
}
