package me.sanao1006.feature.antenna

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuitx.effects.LaunchedImpressionEffect
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.launch
import me.sanao1006.core.domain.antenna.CreateAntennaUseCase
import me.sanao1006.core.domain.antenna.DeleteAntennaUseCase
import me.sanao1006.core.domain.antenna.UpdateAntennaUseCase
import me.sanao1006.core.model.uistate.AntennaPostScreenUiState
import me.sanao1006.screens.AntennaPostScreen
import me.sanao1006.screens.AntennaPostScreenType
import me.sanao1006.screens.event.globalIcon.GlobalIconEventPresenter
import me.snao1006.res_value.ResString

class AntennaPostScreenPresenter @AssistedInject constructor(
    @Assisted private val screen: AntennaPostScreen,
    private val createAntennaUseCase: CreateAntennaUseCase,
    private val deleteAntennaUseCase: DeleteAntennaUseCase,
    private val updateAntennaUseCase: UpdateAntennaUseCase,
    private val globalIconEventPresenter: GlobalIconEventPresenter
) : Presenter<AntennaPostScreen.State> {
    @Composable
    override fun present(): AntennaPostScreen.State {
        val globalIconEventState = globalIconEventPresenter.present()

        val postScreenType = if (screen.antenna == null) {
            AntennaPostScreenType.CREATE
        } else {
            AntennaPostScreenType.EDIT
        }
        val isEdit = screen.antenna != null
        val scope = rememberCoroutineScope()
        val context = LocalContext.current

        var uiState: AntennaPostScreenUiState by rememberRetained {
            mutableStateOf(AntennaPostScreenUiState())
        }

        LaunchedImpressionEffect(Unit) {
            screen.antenna?.let { antenna ->
                uiState = uiState.copy(
                    antennaName = antenna.name,
                    isBotAccountExcluded = antenna.excludeBots,
                    antennaSource = antenna.src,
                    users = antenna.users,
                    isLocalOnly = antenna.localOnly,
                    isReplyIncluded = antenna.withReplies,
                    keywordValue = antenna.keywords.joinToString("\n") { it.joinToString(" ") },
                    exceptedKeywordValue = antenna.excludeKeywords.joinToString("\n") {
                        it.joinToString(" ")
                    },
                    isCaseSensitive = antenna.caseSensitive,
                    isOnlyFileNote = antenna.withFile
                )
            }
        }

        return AntennaPostScreen.State(
            uiState = uiState,
            screenType = postScreenType,
            isEdit = isEdit,
            globalIconEventSink = globalIconEventState.eventSink
        ) { event ->
            when (event) {
                is AntennaPostScreen.Event.OnAntennaNameChange -> {
                    uiState = uiState.copy(antennaName = event.name)
                }

                is AntennaPostScreen.Event.OnExpandClick -> {
                    uiState = uiState.copy(expanded = event.isExpanded)
                }

                is AntennaPostScreen.Event.OnDropDownItemClick -> {
                    uiState = uiState.copy(
                        expanded = false,
                        antennaSource = event.antennaSource
                    )
                }

                is AntennaPostScreen.Event.OnUsersNameChange -> {
                    uiState = if (uiState.antennaSource != event.antennaSource) {
                        uiState.copy(users = null)
                    } else {
                        uiState.copy(users = event.users.split("\n"))
                    }
                }

                is AntennaPostScreen.Event.OnBotAccountExcludedChange -> {
                    uiState = uiState.copy(isBotAccountExcluded = event.isBotAccountExcluded)
                }

                is AntennaPostScreen.Event.OnReplyIncludedChange -> {
                    uiState = uiState.copy(isReplyIncluded = event.isReplyIncluded)
                }

                is AntennaPostScreen.Event.OnKeywordValueChange -> {
                    uiState = uiState.copy(keywordValue = event.keywordValue)
                }

                is AntennaPostScreen.Event.OnExceptedKeywordValueChange -> {
                    uiState = uiState.copy(exceptedKeywordValue = event.exceptedKeywordValue)
                }

                is AntennaPostScreen.Event.OnLocalOnlyChange -> {
                    uiState = uiState.copy(isLocalOnly = event.isLocalOnly)
                }

                is AntennaPostScreen.Event.OnCaseSensitiveChange -> {
                    uiState = uiState.copy(isCaseSensitive = event.isCaseSensitive)
                }

                is AntennaPostScreen.Event.OnOnlyFileNoteChange -> {
                    uiState = uiState.copy(isOnlyFileNote = event.isOnlyFileNote)
                }

                is AntennaPostScreen.Event.OnSaveClick -> {
                    scope.launch {
                        if (isEdit) {
                            screen.antenna?.let {
                                updateAntennaUseCase.invoke(
                                    antennaId = it.id,
                                    name = uiState.antennaName,
                                    src = uiState.antennaSource,
                                    userListId = null,
                                    keywords = uiState.keywordValue.split("\n")
                                        .map { it.split(" ") },
                                    canSensitive = uiState.isCaseSensitive,
                                    withReplies = uiState.isReplyIncluded,
                                    withFile = uiState.isOnlyFileNote,
                                    localOnly = uiState.isLocalOnly,
                                    excludeBots = uiState.isBotAccountExcluded
                                )
                                    .onSuccess {
                                        event.snackbarHostState.showSnackbar(
                                            message = context.getString(ResString.antenna_created_success)
                                        )
                                    }
                                    .onFailure {
                                        event.snackbarHostState.showSnackbar(
                                            context.getString(ResString.antenna_created_failed)
                                        )
                                    }
                            }
                        } else {
                            createAntennaUseCase.invoke(
                                name = uiState.antennaName,
                                src = uiState.antennaSource,
                                userListId = null,
                                keywords = uiState.keywordValue.split("\n")
                                    .map { it.split(" ") },
                                canSensitive = uiState.isCaseSensitive,
                                withReplies = uiState.isReplyIncluded,
                                withFile = uiState.isOnlyFileNote,
                                localOnly = uiState.isLocalOnly,
                                excludeBots = uiState.isBotAccountExcluded
                            )
                                .onSuccess {
                                    event.snackbarHostState.showSnackbar(
                                        message = context.getString(ResString.antenna_created_success)
                                    )
                                }
                                .onFailure {
                                    event.snackbarHostState.showSnackbar(
                                        context.getString(ResString.antenna_created_failed)
                                    )
                                }
                        }
                    }
                }

                is AntennaPostScreen.Event.OnDeleteClick -> {
                    scope.launch {
                        screen.antenna?.let {
                            deleteAntennaUseCase.invoke(it.id)
                                .onSuccess {}
                                .onFailure {}
                        }
                    }
                }
            }
        }
    }

    @AssistedFactory
    @CircuitInject(AntennaPostScreen::class, SingletonComponent::class)
    fun interface Factory {
        fun create(screen: AntennaPostScreen): AntennaPostScreenPresenter
    }
}
