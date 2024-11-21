package me.sanao1006.feature.home

import androidx.compose.runtime.Composable
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.retained.collectAsRetainedState
import com.slack.circuit.runtime.presenter.Presenter
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import me.sanao1006.core.model.home.notes.TimelineUiState
import me.sanao1006.feature.home.domain.FlowNotesTimelineUseCase
import javax.inject.Inject

@CircuitInject(HomeScreen::class, SingletonComponent::class)
class HomeScreenPresenter @Inject constructor(
    private val flowNotesTimelineUseCase: FlowNotesTimelineUseCase
) : Presenter<HomeScreen.State> {
    private val timelineFlow: Flow<List<TimelineUiState>> = flowNotesTimelineUseCase()

    @Composable
    override fun present(): HomeScreen.State {
        val timelineUiState = timelineFlow.collectAsRetainedState(emptyList()).value
        return HomeScreen.State(
            uiState = timelineUiState,
            eventSink = {}
        )
    }
}
