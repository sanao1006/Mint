package me.sanao1006.feature.channel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.presenter.Presenter
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
import kotlinx.coroutines.launch
import me.sanao1006.core.domain.channel.GetChannelListUseCase
import me.sanao1006.core.model.uistate.ChannelListUiState
import me.sanao1006.screens.ChannelListScreen
import me.sanao1006.screens.event.globalIcon.GlobalIconEventPresenter

@CircuitInject(ChannelListScreen::class, SingletonComponent::class)
class ChannelListScreenPresenter @Inject constructor(
    private val globalIconEventPresenter: GlobalIconEventPresenter,
    private val getChannelListUseCase: GetChannelListUseCase
) : Presenter<ChannelListScreen.State> {
    @Composable
    override fun present(): ChannelListScreen.State {
        val globalIconEventState = globalIconEventPresenter.present()

        val scope = rememberCoroutineScope()
        var channelListUiState by rememberRetained { mutableStateOf(ChannelListUiState()) }

        return ChannelListScreen.State(
            channelListUiState = channelListUiState,
            globalIconEventSink = globalIconEventState.eventSink
        ) { event ->
            when (event) {
                is ChannelListScreen.Event.OnChannelClick -> {}

                ChannelListScreen.Event.OnEnterClick -> {
                    scope.launch {
                        val channelList = getChannelListUseCase.invoke(
                            query = channelListUiState.channelName
                        )
                        channelListUiState = channelListUiState.copy(channelList = channelList)
                    }
                }

                is ChannelListScreen.Event.OnChannelNameChange -> {
                    channelListUiState = channelListUiState.copy(channelName = event.channelName)
                }

                ChannelListScreen.Event.OnSearchClick -> {
                    channelListUiState = channelListUiState.copy(selectedTabIndex = 0)
                }

                ChannelListScreen.Event.OnTrendClick -> {
                    channelListUiState = channelListUiState.copy(selectedTabIndex = 1)
                }

                ChannelListScreen.Event.OnFavoriteClick -> {
                    channelListUiState = channelListUiState.copy(selectedTabIndex = 2)
                }

                is ChannelListScreen.Event.OnPageChange -> {
                    channelListUiState = channelListUiState.copy(selectedTabIndex = event.page)
                }
            }
        }
    }
}
