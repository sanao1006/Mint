package me.sanao1006.feature.login

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen
import dagger.hilt.components.SingletonComponent
import kotlinx.parcelize.Parcelize

@Parcelize
data object LoginScreen : Screen {
    data class State(
        val email: String = "",
        val password: String = "",
        val isLoading: Boolean = false,
        val error: String = ""
    ) : CircuitUiState
}

@CircuitInject(LoginScreen::class, SingletonComponent::class)
@Composable
fun LoginScreenUi(state: LoginScreen.State, modifier: Modifier) {
    Column {
        Text("login screen")
    }
}
