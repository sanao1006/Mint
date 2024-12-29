package me.sanao1006.feature.antenna

import android.content.Context
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.foundation.rememberAnsweringNavigator
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuitx.effects.LaunchedImpressionEffect
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
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
import me.snao1006.res_value.ResString

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
        val context = LocalContext.current
        val navigator = LocalNavigator.current
        var isSuccessPostAntenna: Boolean? by remember { mutableStateOf(null) }
        var screenName: String by rememberRetained { mutableStateOf("") }

        var uiState: AntennaScreenUiState by rememberRetained {
            mutableStateOf(AntennaScreenUiState.Loading)
        }
        var openDialog by remember { mutableStateOf(false) }
        var selectedAntennaId by remember { mutableStateOf<String?>(null) }
        val resultNavigator =
            rememberAnsweringNavigator<AntennaScreen.Result>(navigator) { result ->
                isSuccessPostAntenna = result.success
                screenName = result.screenName
            }

        LaunchedImpressionEffect(Unit) {
            uiState = getAntennasUseCase.invoke()
        }

        return AntennaScreen.State(
            uiState = uiState,
            openDialog = openDialog,
            isSuccessPostAntenna = isSuccessPostAntenna,
            screenName = screenName,
            selectedAntennaId = selectedAntennaId,
            globalIconEventSink = globalEventState.eventSink
        ) { event ->
            when (event) {
                is AntennaScreen.Event.OnAntennaClick -> {
                    navigator.goTo(AntennaListScreen(event.id))
                }

                is AntennaScreen.Event.OnEditClick -> {
                    resultNavigator.goTo(AntennaPostScreen(event.antenna))
                }

                is AntennaScreen.Event.OnDeleteButtonClick -> {
                    openDialog = true
                    selectedAntennaId = event.id
                }

                is AntennaScreen.Event.OnDeleteClick -> {
                    scope.launch {
                        openDialog = false
                        suspendRunCatching {
                            deleteAntennaUseCase.invoke(event.id)
                        }.onSuccess {
                            uiState = getAntennasUseCase.invoke()
                            event.snackbarHostState.showSnackbar(
                                context.getString(ResString.antenna_deleted_success)
                            )
                        }.onFailure {
                            event.snackbarHostState.showSnackbar(
                                context.getString(ResString.antenna_deleted_failed)
                            )
                        }
                    }
                }

                AntennaScreen.Event.OnDialogHideClick -> {
                    openDialog = false
                }

                is AntennaScreen.Event.OnCreateClick -> {
                    resultNavigator.goTo(AntennaPostScreen())
                }

                is AntennaScreen.Event.OnAntennaPost -> {
                    scope.launch {
                        uiState = getAntennasUseCase.invoke()
                        isSuccessPostAntenna?.let { success ->
                            showSnackbarMessage(
                                success = success,
                                screenName = screenName,
                                snackbarHostState = event.snackbarHostState,
                                context = context
                            )
                        }
                    }
                }
            }
        }
    }
}

private suspend fun showSnackbarMessage(
    success: Boolean,
    screenName: String,
    snackbarHostState: SnackbarHostState,
    context: Context
) {
    if (success) {
        when (screenName) {
            "create" -> {
                snackbarHostState.showSnackbar(
                    context.getString(ResString.antenna_created_success)
                )
            }

            "update" -> {
                snackbarHostState.showSnackbar(
                    context.getString(ResString.antenna_updated_success)
                )
            }

            "delete" -> {
                snackbarHostState.showSnackbar(
                    context.getString(ResString.antenna_deleted_success)
                )
            }

            else -> { // Do Nothing
            }
        }
        return
    }
    when (screenName) {
        "create" -> {
            snackbarHostState.showSnackbar(
                context.getString(ResString.antenna_created_failed)
            )
        }

        "update" -> {
            snackbarHostState.showSnackbar(
                context.getString(ResString.antenna_updated_failed)
            )
        }

        "delete" -> {
            snackbarHostState.showSnackbar(
                context.getString(ResString.antenna_deleted_failed)
            )
        }

        else -> {}
    }
}
