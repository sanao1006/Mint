package me.sanao1006.screens.event.globalIcon

import androidx.compose.material3.DrawerState
import com.slack.circuit.runtime.CircuitUiEvent
import kotlinx.coroutines.CoroutineScope

sealed class GlobalIconEvent : CircuitUiEvent {
    data class OnGlobalIconClicked(
        val drawerState: DrawerState,
        val scope: CoroutineScope
    ) : GlobalIconEvent()

    data object OnArrowBackIconClicked : GlobalIconEvent()
}