package me.sanao1006.feature.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
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
import me.sanao1006.core.model.home.notes.TimelineUiState

@Parcelize
data object HomeScreen : Screen {
    @Immutable
    data class State(
        val uiState: List<TimelineUiState?> = listOf(),
        val eventSink: (Event) -> Unit
    ) : CircuitUiState

    sealed class Event : CircuitUiEvent {
        data object OnButtonClicked : Event()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@CircuitInject(HomeScreen::class, SingletonComponent::class)
@Composable
fun HomeScreenUi(state: HomeScreen.State, modifier: Modifier) {
    Box(modifier = modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text("Home")
                    }
                )
            }
        ) {
            LazyColumn(modifier = Modifier.padding(it)) {
                items(state.uiState) {
                    Text(text = it?.user?.name ?: "")
                    Text(text = it?.text ?: "")
                }
            }
        }
    }
}
