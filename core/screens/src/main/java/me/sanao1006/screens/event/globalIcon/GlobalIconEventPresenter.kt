package me.sanao1006.screens.event.globalIcon

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.presenter.Presenter
import javax.inject.Inject
import kotlinx.coroutines.launch
import me.sanao1006.core.data.compositionLocal.LocalNavigator
import me.sanao1006.screens.HomeScreen

data class DrawerEventState(
    val eventSink: (GlobalIconEvent) -> Unit
) : CircuitUiState

class GlobalIconEventPresenter @Inject constructor() : Presenter<DrawerEventState> {
    @Composable
    override fun present(): DrawerEventState {
        val scope = rememberCoroutineScope()
        val navigator = LocalNavigator.current
        return DrawerEventState { event ->
            when (event) {
                is GlobalIconEvent.OnGlobalIconClicked -> {
                    scope.launch {
                        event.drawerState.apply {
                            if (isClosed) open() else close()
                        }
                    }
                }

                GlobalIconEvent.OnArrowBackIconClicked -> {
                    navigator.resetRoot(HomeScreen)
                }

                GlobalIconEvent.OnBackBeforeScreen -> {
                    navigator.pop()
                }
            }
        }
    }
}
