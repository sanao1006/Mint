package me.sanao1006.feature.home

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.foundation.rememberAnsweringNavigator
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.sanao1006.core.model.notes.TimelineUiState
import me.sanao1006.feature.home.domain.GetNotesTimelineUseCase
import me.sanao1006.feature.home.domain.TimelineType
import me.sanao1006.screens.HomeScreen
import me.sanao1006.screens.NoteScreen
import me.snao1006.res_value.ResString

class HomeScreenPresenter @AssistedInject constructor(
    @Assisted private val navigator: Navigator,
    private val getNotesTimelineUseCase: GetNotesTimelineUseCase,
) : Presenter<HomeScreen.State> {

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    override fun present(): HomeScreen.State {
        var isSuccessCreateNote: Boolean? by rememberRetained { mutableStateOf(null) }

        val context = LocalContext.current
        val scope = rememberCoroutineScope()
        val nav = rememberAnsweringNavigator<NoteScreen.Result>(navigator) { result ->
            isSuccessCreateNote = result.success
        }

        var timelineType by rememberRetained { mutableStateOf(TimelineType.SOCIAL) }
        var timelineUiState: List<TimelineUiState> by rememberRetained(timelineType) {
            mutableStateOf(emptyList())
        }

        var isRefreshed by remember { mutableStateOf(false) }
        val pullRefreshState = rememberPullRefreshState(
            refreshing = isRefreshed,
            onRefresh = {
                scope.launch {
                    isRefreshed = true
                    timelineUiState = getNotesTimelineUseCase(timelineType)
                    delay(1500L)
                    isRefreshed = false
                }
            },
            refreshThreshold = 50.dp,
            refreshingOffset = 50.dp
        )
        LaunchedEffect(Unit) {
            timelineUiState = getNotesTimelineUseCase(timelineType)
        }

        return HomeScreen.State(
            uiState = timelineUiState,
            navigator = navigator,
            isSuccessCreateNote = isSuccessCreateNote,
            pullToRefreshState = pullRefreshState,
            isRefreshed = isRefreshed
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
