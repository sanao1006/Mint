package me.sanao1006.feature.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.parcelize.Parcelize

@Parcelize
data object LoginScreen : Screen {
    @Immutable
    data class State(
        val domain: String = "",
        val error: String = "",
        val authState: AuthStateType = AuthStateType.FIXED,
        val eventSink: (Event) -> Unit
    ) : CircuitUiState

    sealed class Event : CircuitUiEvent {
        data class OnTextChanged(val text: String) : Event()
        data class OnButtonClicked(val scope: CoroutineScope) : Event()
        data class OnAuthButtonClicked(val scope: CoroutineScope) : Event()
    }
}

@CircuitInject(LoginScreen::class, SingletonComponent::class)
@Composable
fun LoginScreenUi(state: LoginScreen.State, modifier: Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(vertical = 64.dp, horizontal = 24.dp), contentAlignment = Alignment.TopCenter
    ) {
        LoginContent(state = state)
    }
}

@Composable
private fun LoginContent(state: LoginScreen.State) {
    Column(
        modifier = Modifier.padding(64.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        val scope = rememberCoroutineScope()
        Text(
            text = "Enter domain",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.padding(8.dp))
        TextField(
            value = state.domain,
            maxLines = 1,
            onValueChange = { state.eventSink(LoginScreen.Event.OnTextChanged(it)) }
        )
        Spacer(modifier = Modifier.padding(8.dp))
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = { state.eventSink(LoginScreen.Event.OnButtonClicked(scope)) }
        ) {
            Text("OK")
        }
        if (state.authState == AuthStateType.WAITING) {
            Spacer(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
            )
            Button(
                modifier = Modifier
                    .fillMaxWidth(),
                onClick = { state.eventSink(LoginScreen.Event.OnAuthButtonClicked(scope)) }
            ) {
                Text("Auth")
            }
        }
    }
}

@PreviewLightDark
@Composable
fun PreviewLoginScreenUi() {
    LoginScreenUi(
        state = LoginScreen.State(
            domain = "example.com",
            eventSink = {}
        ),
        modifier = Modifier
    )
}