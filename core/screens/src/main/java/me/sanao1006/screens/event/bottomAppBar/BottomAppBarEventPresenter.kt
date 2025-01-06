package me.sanao1006.screens.event.bottomAppBar

import androidx.compose.runtime.Composable
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.presenter.Presenter
import javax.inject.Inject
import me.sanao1006.core.data.compositionLocal.LocalNavigator
import me.sanao1006.screens.HomeScreen
import me.sanao1006.screens.NotificationScreen

data class BottomAppBarState(
    val eventSink: (BottomAppBarActionEvent) -> Unit
) : CircuitUiState

class BottomAppBarPresenter @Inject constructor() : Presenter<BottomAppBarState> {
    @Composable
    override fun present(): BottomAppBarState {
        val navigator = LocalNavigator.current
        return BottomAppBarState(
            eventSink = { event ->
                when (event) {
                    BottomAppBarActionEvent.OnHomeIconClicked -> {
                        navigator.resetRoot(HomeScreen, true, true)
                    }

                    BottomAppBarActionEvent.OnNotificationIconClicked -> {
                        navigator.resetRoot(NotificationScreen, true, true)
                    }
                }
            }
        )
    }
}
