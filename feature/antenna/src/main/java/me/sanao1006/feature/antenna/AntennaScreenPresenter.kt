package me.sanao1006.feature.antenna

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.presenter.Presenter
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
import me.sanao1006.core.model.uistate.AnnouncementUiState
import me.sanao1006.screens.AntennaScreen

@CircuitInject(AntennaScreen::class, SingletonComponent::class)
class AntennaScreenPresenter @Inject constructor() : Presenter<AntennaScreen.State> {
    @Composable
    override fun present(): AntennaScreen.State {
        var uiState by rememberRetained { mutableStateOf(AnnouncementUiState.Loading) }
        return AntennaScreen.State(
            uiState = uiState
        ) { event ->
            when (event) {
                is AntennaScreen.Event.OnAntennaClick -> {}
                is AntennaScreen.Event.OnEditClick -> {}
                is AntennaScreen.Event.OnCreateClick -> {}
                is AntennaScreen.Event.OnBackClick -> {}
            }
        }
    }
}
