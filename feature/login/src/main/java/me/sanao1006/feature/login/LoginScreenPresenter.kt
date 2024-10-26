package me.sanao1006.feature.login

import androidx.compose.runtime.Composable
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.presenter.Presenter
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject

@CircuitInject(LoginScreen::class, SingletonComponent::class)
class LoginScreenPresenter @Inject constructor(): Presenter<LoginScreen.State> {
    @Composable
    override fun present(): LoginScreen.State {
        return LoginScreen.State()
    }
}