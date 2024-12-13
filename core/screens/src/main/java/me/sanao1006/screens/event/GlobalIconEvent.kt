package me.sanao1006.screens.event

import androidx.compose.material3.DrawerState
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.popUntil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import me.sanao1006.screens.HomeScreen

sealed class GlobalIconEvent : CircuitUiEvent {
    data class OnGlobalIconClicked(
        val drawerState: DrawerState,
        val scope: CoroutineScope
    ) : GlobalIconEvent()

    data object OnArrowBackIconClicked : GlobalIconEvent()
}

fun GlobalIconEvent.handleNavigationIconClicked(navigator: Navigator) {
    when (this) {
        is GlobalIconEvent.OnGlobalIconClicked -> {
            this.scope.launch {
                this@handleNavigationIconClicked.drawerState.apply {
                    if (isClosed) open() else close()
                }
            }
        }

        GlobalIconEvent.OnArrowBackIconClicked -> {
            navigator.popUntil { it is HomeScreen }
        }
    }
}
