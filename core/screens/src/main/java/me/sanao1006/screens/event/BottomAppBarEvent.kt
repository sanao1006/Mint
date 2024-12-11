package me.sanao1006.screens.event

import com.slack.circuit.runtime.Navigator
import me.sanao1006.screens.HomeScreen
import me.sanao1006.screens.SearchScreen

sealed class BottomAppBarActionEvent {
    data object OnHomeIconClicked : BottomAppBarActionEvent()
    data object OnSearchIconClicked : BottomAppBarActionEvent()
    data object OnNotificationIconClicked : BottomAppBarActionEvent()
}

fun BottomAppBarActionEvent.handleBottomAppBarActionEvent(
    navigator: Navigator
) {
    when (this) {
        BottomAppBarActionEvent.OnHomeIconClicked -> {
            navigator.goTo(HomeScreen)
        }

        BottomAppBarActionEvent.OnSearchIconClicked -> {
            navigator.goTo(SearchScreen)
        }

        BottomAppBarActionEvent.OnNotificationIconClicked -> {
            // TODO goto Notification Screen
        }
    }
}
