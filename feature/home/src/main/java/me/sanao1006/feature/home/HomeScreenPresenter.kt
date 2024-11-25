package me.sanao1006.feature.home

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.retained.collectAsRetainedState
import com.slack.circuit.runtime.presenter.Presenter
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonPrimitive
import me.sanao1006.core.model.home.notes.TimelineUiState
import me.sanao1006.core.model.home.notes.User
import me.sanao1006.feature.home.domain.GetNotesTimelineUseCase
import me.sanao1006.misskey_streaming.StreamingChannel
import me.sanao1006.misskey_streaming.WebsocketRepository
import me.sanao1006.misskey_streaming.model.StreamingResponse
import javax.inject.Inject

@CircuitInject(HomeScreen::class, SingletonComponent::class)
class HomeScreenPresenter @Inject constructor(
    private val websocketRepository: WebsocketRepository,
    private val getNotesTimelineUseCase: GetNotesTimelineUseCase,
    private val json: Json
) : Presenter<HomeScreen.State> {
    private val websocketRepositoryFlow: Flow<StreamingResponse> =
        websocketRepository.getSessionStream()

    @SuppressLint("ProduceStateDoesNotAssignValue")
    @Composable
    override fun present(): HomeScreen.State {
        val scope = rememberCoroutineScope()
        val timelineUiState by produceState<List<TimelineUiState>>(emptyList()) {
            value = getNotesTimelineUseCase()
        }

        val streaming: State<StreamingResponse> =
            websocketRepositoryFlow.collectAsRetainedState(StreamingResponse())

        LifecycleEventEffect(event = Lifecycle.Event.ON_PAUSE) {
            scope.launch {
                websocketRepository.close()
            }
        }

        LaunchedEffect(Unit) {
            websocketRepository.sendAction(streamingChannel = StreamingChannel.SOCIAL)
        }

        val combinedList: List<TimelineUiState> by produceState(
            emptyList(),
            timelineUiState,
            streaming.value
        ) {
            val text = streaming.value.body.body?.get("text")?.jsonPrimitive?.content ?: ""
            val user = json.decodeFromString<User>(
                streaming.value.body.body?.get("user")?.toString() ?: "{}"
            )

            value = listOf(
                TimelineUiState(
                    text = text,
                    user = user,
                )
            ) + timelineUiState
        }

        return HomeScreen.State(
            uiState = combinedList,
            eventSink = {}
        )
    }
}
