package me.sanao1006.screens.event.bottomAppBar

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.presenter.Presenter
import javax.inject.Inject
import kotlinx.coroutines.launch
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
        val scope = rememberCoroutineScope()
        return BottomAppBarState(
            eventSink = { event ->
                when (event) {
                    is BottomAppBarActionEvent.OnHomeIconClicked -> {
                        if (navigator.peek() == HomeScreen) {
                            scope.launch {
                                event.lazyListState.animateScrollToItem(0)
                            }
                        } else {
                            navigator.resetRoot(HomeScreen, true, true)
                        }
                    }

                    is BottomAppBarActionEvent.OnNotificationIconClicked -> {
                        if (navigator.peek() == NotificationScreen) {
                            scope.launch {
                                event.lazyListState.animateScrollToItem(0)
                            }
                        } else {
                            navigator.resetRoot(NotificationScreen, true, true)
                        }
                    }
                }
            }
        )
    }
}
