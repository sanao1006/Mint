package me.sanao1006.feature.antenna

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuitx.effects.LaunchedImpressionEffect
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.components.SingletonComponent
import me.sanao1006.core.model.uistate.AntennaPostScreenUiState
import me.sanao1006.screens.AntennaPostScreen
import me.sanao1006.screens.AntennaPostScreenType
import me.sanao1006.screens.event.globalIcon.GlobalIconEventPresenter

class AntennaPostScreenPresenter @AssistedInject constructor(
    @Assisted private val screen: AntennaPostScreen,
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
            isEdit = screen.antenna != null,
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
                    // Save the antenna
                }

                is AntennaPostScreen.Event.OnDeleteClick -> {
                    // Delete the antenna
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
