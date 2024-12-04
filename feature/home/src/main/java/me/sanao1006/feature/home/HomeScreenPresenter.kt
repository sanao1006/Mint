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
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.foundation.rememberAnsweringNavigator
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
import me.sanao1006.feature.home.domain.GetNotesTimelineUseCase
import me.sanao1006.feature.home.domain.TimelineType
import me.sanao1006.misskey_streaming.StreamingChannel
import me.sanao1006.misskey_streaming.WebsocketRepository
import me.sanao1006.misskey_streaming.model.StreamingResponse
import me.sanao1006.screens.HomeScreen
import me.sanao1006.screens.NoteScreen
import me.snao1006.res_value.ResString

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
        var isSuccessCreateNote: Boolean? by rememberRetained { mutableStateOf(null) }
        val context = LocalContext.current
        val nav = rememberAnsweringNavigator<NoteScreen.Result>(navigator) { result ->
            isSuccessCreateNote = result.success
        }

        val scope = rememberCoroutineScope()
        var timelineType by rememberRetained { mutableStateOf(TimelineType.SOCIAL) }
        val timelineUiState by produceState<List<me.sanao1006.core.model.notes.TimelineUiState>>(
            emptyList(),
            timelineType
        ) {
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

        val combinedList: List<me.sanao1006.core.model.notes.TimelineUiState> by produceState(
            emptyList(),
            timelineUiState,
            streaming.value
        ) {
            val text = streaming.value.body.body?.get("text")?.jsonPrimitive?.content ?: ""
            val user = json.decodeFromString<me.sanao1006.core.model.notes.User>(
                streaming.value.body.body?.get("user")?.toString() ?: "{}"
            )

            val mutableList = mutableListOf<me.sanao1006.core.model.notes.TimelineUiState>(
                me.sanao1006.core.model.notes.TimelineUiState(
                    text = text,
                    user = user
                )
            )
            mutableList.addAll(timelineUiState)

            value = mutableList.toList()
        }

        return HomeScreen.State(
            uiState = combinedList,
            navigator = navigator,
            isSuccessCreateNote = isSuccessCreateNote
        ) { event ->
            when (event) {
                is HomeScreen.Event.OnNoteCreated -> {
                    isSuccessCreateNote?.let { flg ->
                        event.scope.launch {
                            event.snackbarHostState.showSnackbar(
                                message = if (flg) {
                                    context.getString(ResString.post_result_message_success)
                                } else {
                                    context.getString(ResString.post_result_message_failed)
                                }
                            )
                        }
                    }
                }

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
                    nav.goTo(NoteScreen)
//                    navigator.goTo(NoteScreen)
                }
            }
        }
    }
}

@CircuitInject(HomeScreen::class, SingletonComponent::class)
@AssistedFactory
fun interface Factory {
    fun create(navigator: Navigator): HomeScreenPresenter
}
