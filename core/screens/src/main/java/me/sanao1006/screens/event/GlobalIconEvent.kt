package me.sanao1006.screens.event

import androidx.compose.material3.DrawerState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

sealed class GlobalIconEvent {
    data class OnGlobalIconClicked(
        val drawerState: DrawerState,
        val scope: CoroutineScope
    ) : GlobalIconEvent()
}

fun GlobalIconEvent.handleNavigationIconClicked() {
    when (this) {
        is GlobalIconEvent.OnGlobalIconClicked -> {
            this.scope.launch {
                this@handleNavigationIconClicked.drawerState.apply {
                    if (isClosed) open() else close()
                }
            }
        }
    }
}
