package me.sanao1006.feature.announcement

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuitx.effects.LaunchedImpressionEffect
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
import me.sanao1006.core.domain.announcement.GetAnnouncementsUseCase
import me.sanao1006.core.model.uistate.AnnouncementUiState
import me.sanao1006.screens.AnnouncementScreen
import me.sanao1006.screens.event.GlobalIconEventPresenter

@CircuitInject(AnnouncementScreen::class, SingletonComponent::class)
class AnnouncementPresenter @Inject constructor(
    private val getAnnouncementsUseCase: GetAnnouncementsUseCase,
    private val globalIconEventPresenter: GlobalIconEventPresenter
) : Presenter<AnnouncementScreen.State> {
    @Composable
    override fun present(): AnnouncementScreen.State {
        val globalIconEventState = globalIconEventPresenter.present()

        var isActive by rememberRetained { mutableStateOf(true) }
        var selectedTabIndex by remember { mutableIntStateOf(0) }
        val announcementItemExpandedStates =
            rememberRetained { mutableStateMapOf<String, Boolean>() }
        var uiState: AnnouncementUiState by rememberRetained {
            mutableStateOf(AnnouncementUiState.Loading)
        }

        LaunchedImpressionEffect(Unit) {
            uiState = getAnnouncementsUseCase()
        }

        LaunchedImpressionEffect(isActive) {
            uiState = getAnnouncementsUseCase(isActive = isActive)
        }

        return AnnouncementScreen.State(
            uiState = uiState,
            selectedTabIndex = selectedTabIndex,
            announcementItemExpandedStates = announcementItemExpandedStates,
            globalIconEventSink = globalIconEventState.eventSink
        ) { event ->
            when (event) {
                is AnnouncementScreen.Event.OnTabClicked -> {
                    selectedTabIndex = event.index
                    isActive = event.index == 0
                }

                is AnnouncementScreen.Event.OnCardClicked -> {
                    // Whether the tapped announcement is expanded or not
                    val isExpanded = announcementItemExpandedStates[event.id] == true
                    // Update the expanded state of the tapped announcement
                    announcementItemExpandedStates[event.id] = !isExpanded
                }
            }
        }
    }
}
