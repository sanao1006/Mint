package me.sanao1006.feature.channel

import androidx.compose.runtime.Composable
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.presenter.Presenter
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
import me.sanao1006.screens.ChannelListScreen
import me.sanao1006.screens.event.globalIcon.GlobalIconEventPresenter

@CircuitInject(ChannelListScreen::class, SingletonComponent::class)
class ChannelListScreenPresenter @Inject constructor(
    private val globalIconEventPresenter: GlobalIconEventPresenter
) : Presenter<ChannelListScreen.State> {
    @Composable
    override fun present(): ChannelListScreen.State {
        val globalIconEventState = globalIconEventPresenter.present()

        return ChannelListScreen.State(
            globalIconEventSink = globalIconEventState.eventSink
        ) { event ->
            when (event) {
                is ChannelListScreen.Event.OnChannelClick -> {}
            }
        }
    }
}
