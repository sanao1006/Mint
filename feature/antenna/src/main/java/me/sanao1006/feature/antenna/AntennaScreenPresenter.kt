package me.sanao1006.feature.antenna

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuitx.effects.LaunchedImpressionEffect
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
import me.sanao1006.core.domain.antenna.GetAntennasUseCase
import me.sanao1006.core.model.uistate.AntennaScreenUiState
import me.sanao1006.screens.AntennaScreen
import me.sanao1006.screens.event.globalIcon.GlobalIconEventPresenter

@CircuitInject(AntennaScreen::class, SingletonComponent::class)
class AntennaScreenPresenter @Inject constructor(
    private val getAntennasUseCase: GetAntennasUseCase,
    private val globalIconEventPresenter: GlobalIconEventPresenter
) : Presenter<AntennaScreen.State> {
    @Composable
    override fun present(): AntennaScreen.State {
        val globalEventState = globalIconEventPresenter.present()

        var uiState: AntennaScreenUiState by rememberRetained {
            mutableStateOf(AntennaScreenUiState.Loading)
        }

        LaunchedImpressionEffect(Unit) {
            uiState = getAntennasUseCase.invoke()
        }

        return AntennaScreen.State(
            uiState = uiState,
            globalIconEventSink = globalEventState.eventSink
        ) { event ->
            when (event) {
                is AntennaScreen.Event.OnAntennaClick -> {}
                is AntennaScreen.Event.OnEditClick -> {}
                is AntennaScreen.Event.OnDeleteClick -> {}
                is AntennaScreen.Event.OnCreateClick -> {}
            }
        }
    }
}
