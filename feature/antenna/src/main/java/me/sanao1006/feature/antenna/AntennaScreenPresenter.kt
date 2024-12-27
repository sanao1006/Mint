package me.sanao1006.feature.antenna

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuitx.effects.LaunchedImpressionEffect
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.launch
import me.sanao1006.core.data.compositionLocal.LocalNavigator
import me.sanao1006.core.data.util.suspendRunCatching
import me.sanao1006.core.domain.antenna.DeleteAntennaUseCase
import me.sanao1006.core.domain.antenna.GetAntennasUseCase
import me.sanao1006.core.model.uistate.AntennaScreenUiState
import me.sanao1006.screens.AntennaListScreen
import me.sanao1006.screens.AntennaPostScreen
import me.sanao1006.screens.AntennaScreen
import me.sanao1006.screens.event.globalIcon.GlobalIconEventPresenter
import javax.inject.Inject

@CircuitInject(AntennaScreen::class, SingletonComponent::class)
class AntennaScreenPresenter @Inject constructor(
    private val getAntennasUseCase: GetAntennasUseCase,
    private val deleteAntennaUseCase: DeleteAntennaUseCase,
    private val globalIconEventPresenter: GlobalIconEventPresenter
) : Presenter<AntennaScreen.State> {
    @Composable
    override fun present(): AntennaScreen.State {
        val globalEventState = globalIconEventPresenter.present()

        val scope = rememberCoroutineScope()
        val navigator = LocalNavigator.current
        var uiState: AntennaScreenUiState by rememberRetained {
            mutableStateOf(AntennaScreenUiState.Loading)
        }
        var openDialog by remember { mutableStateOf(false) }
        var selectedAntennaId by remember { mutableStateOf<String?>(null) }

        LaunchedImpressionEffect(Unit) {
            uiState = getAntennasUseCase.invoke()
        }

        return AntennaScreen.State(
            uiState = uiState,
            openDialog = openDialog,
            selectedAntennaId = selectedAntennaId,
            globalIconEventSink = globalEventState.eventSink
        ) { event ->
            when (event) {
                is AntennaScreen.Event.OnAntennaClick -> {
                    navigator.goTo(AntennaListScreen(event.id))
                }

                is AntennaScreen.Event.OnEditClick -> {
                    navigator.goTo(AntennaPostScreen(event.antenna))
                }

                is AntennaScreen.Event.OnDeleteButtonClick -> {
                    openDialog = true
                    selectedAntennaId = event.id
                }

                is AntennaScreen.Event.OnDeleteClick -> {
                    scope.launch {
                        suspendRunCatching {
                            deleteAntennaUseCase.invoke(event.id)
                        }.onSuccess {
                            uiState = getAntennasUseCase.invoke()
                        }
                        openDialog = false
                    }
                }

                AntennaScreen.Event.OnDialogHideClick -> {
                    openDialog = false
                }

                is AntennaScreen.Event.OnCreateClick -> {
                    navigator.goTo(AntennaPostScreen())
                }
            }
        }
    }
}
