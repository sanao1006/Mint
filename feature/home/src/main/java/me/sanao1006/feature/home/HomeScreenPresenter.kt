package me.sanao1006.feature.home

import androidx.compose.runtime.Composable
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.presenter.Presenter
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject

@CircuitInject(HomeScreen::class, SingletonComponent::class)
class HomeScreenPresenter @Inject constructor(
    
) : Presenter<HomeScreen.State> {

    @Composable
    override fun present(): HomeScreen.State {
        return HomeScreen.State(
            title = "Home",
            eventSink = {}
        )
    }
}