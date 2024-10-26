package me.sanao1006.feature.login

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.presenter.Presenter
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject

@CircuitInject(LoginScreen::class, SingletonComponent::class)
class LoginScreenPresenter @Inject constructor() : Presenter<LoginScreen.State> {
    @Composable
    override fun present(): LoginScreen.State {
        var domain by rememberRetained { mutableStateOf("") }
        return LoginScreen.State(
            domain = domain
        ) { event ->
            when (event) {
                is LoginScreen.Event.OnTextChanged -> {
                    domain = event.text
                }
            }
        }
    }
}