package me.sanao1006.feature.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.presenter.Presenter
import dagger.hilt.components.SingletonComponent
import me.sanao1006.feature.home.domain.FlowNotesTimelineUseCase
import javax.inject.Inject

@CircuitInject(HomeScreen::class, SingletonComponent::class)
class HomeScreenPresenter @Inject constructor(
    private val flowNotesTimelineUseCase: FlowNotesTimelineUseCase
) : Presenter<HomeScreen.State> {
    @Composable
    override fun present(): HomeScreen.State {
        val timelineUiState = flowNotesTimelineUseCase().collectAsState(initial = emptyList()).value
        return HomeScreen.State(
            uiState = timelineUiState,
            eventSink = {}
        )
    }
}
