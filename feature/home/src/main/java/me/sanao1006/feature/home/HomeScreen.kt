package me.sanao1006.feature.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen
import dagger.hilt.components.SingletonComponent
import kotlinx.parcelize.Parcelize

@Parcelize
data object HomeScreen : Screen {
    @Immutable
    data class State(
        val title: String = "Home",
        val eventSink: (Event) -> Unit
    ) : CircuitUiState

    sealed class Event : CircuitUiEvent {
        data object OnButtonClicked : Event()
    }
}

@CircuitInject(HomeScreen::class, SingletonComponent::class)
@Composable
fun HomeScreenUi(state: HomeScreen.State, modifier: Modifier) {
    Box(modifier = modifier.fillMaxSize()) {
        Text("hello world")
    }
}
