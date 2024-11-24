package me.sanao1006.feature.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.rememberCoroutineScope
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.retained.collectAsRetainedState
import com.slack.circuit.runtime.presenter.Presenter
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import me.sanao1006.core.model.home.notes.TimelineUiState
import me.sanao1006.feature.home.domain.FlowNotesTimelineUseCase
import me.sanao1006.misskey_streaming.StreamingChannel
import me.sanao1006.misskey_streaming.WebsocketRepository
import me.sanao1006.misskey_streaming.model.StreamingResponse
import javax.inject.Inject

@CircuitInject(HomeScreen::class, SingletonComponent::class)
class HomeScreenPresenter @Inject constructor(
    private val flowNotesTimelineUseCase: FlowNotesTimelineUseCase,
    private val websocketRepository: WebsocketRepository
) : Presenter<HomeScreen.State> {
    private val timelineFlow: Flow<List<TimelineUiState>> = flowNotesTimelineUseCase()
    private val websocketRepositoryFlow: Flow<StreamingResponse> =
        websocketRepository.getSessionStream()

    @Composable
    override fun present(): HomeScreen.State {
        val timelineUiState = timelineFlow.collectAsRetainedState(emptyList()).value
        val scope = rememberCoroutineScope()
        val streaming: State<StreamingResponse> =
            websocketRepositoryFlow.collectAsRetainedState(StreamingResponse())

        DisposableEffect(Unit) {
            onDispose {
                scope.launch {
                    websocketRepository.close()
                }
            }
        }

        LaunchedEffect(Unit) {
            websocketRepository.sendAction(streamingChannel = StreamingChannel.SOCIAL)
        }

        return HomeScreen.State(
            uiState = timelineUiState,
            eventSink = {}
        )
    }
}
