package me.sanao1006.feature.home

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.retained.collectAsRetainedState
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonPrimitive
import me.sanao1006.core.model.home.notes.TimelineUiState
import me.sanao1006.core.model.home.notes.User
import me.sanao1006.feature.home.domain.GetNotesTimelineUseCase
import me.sanao1006.feature.home.domain.TimelineType
import me.sanao1006.feature.note.NoteScreen
import me.sanao1006.misskey_streaming.StreamingChannel
import me.sanao1006.misskey_streaming.WebsocketRepository
import me.sanao1006.misskey_streaming.model.StreamingResponse

class HomeScreenPresenter @AssistedInject constructor(
    @Assisted private val navigator: Navigator,
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
        var timelineType by rememberRetained { mutableStateOf(TimelineType.SOCIAL) }
        val timelineUiState by produceState<List<TimelineUiState>>(emptyList(), timelineType) {
            value = getNotesTimelineUseCase(timelineType = timelineType)
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

            val mutableList = mutableListOf<TimelineUiState>(
                TimelineUiState(
                    text = text,
                    user = user,
                )
            )
            mutableList.addAll(timelineUiState)

            value = mutableList.toList()
        }

        return HomeScreen.State(
            uiState = combinedList
        ) { event ->
            when (event) {
                HomeScreen.Event.OnLocalTimelineClicked -> {
                    timelineType = TimelineType.LOCAL
                }

                HomeScreen.Event.OnSocialTimelineClicked -> {
                    timelineType = TimelineType.SOCIAL
                }

                HomeScreen.Event.OnGlobalTimelineClicked -> {
                    timelineType = TimelineType.GLOBAL
                }

                HomeScreen.Event.OnNoteCreateClicked -> {
                    navigator.goTo(NoteScreen)
                }
            }
        }
    }

}

@CircuitInject(HomeScreen::class, SingletonComponent::class)
@AssistedFactory
interface Factory {
    fun create(navigator: Navigator): HomeScreenPresenter
}
