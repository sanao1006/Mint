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
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuitx.effects.LaunchedImpressionEffect
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.components.SingletonComponent
import me.sanao1006.core.domain.announcement.GetAnnouncementsUseCase
import me.sanao1006.core.model.uistate.AnnouncementUiState
import me.sanao1006.screens.AnnouncementScreen
import me.sanao1006.screens.event.handleNavigationIconClicked

class AnnouncementPresenter @AssistedInject constructor(
    @Assisted private val navigator: Navigator,
    private val getAnnouncementsUseCase: GetAnnouncementsUseCase
) : Presenter<AnnouncementScreen.State> {
    @Composable
    override fun present(): AnnouncementScreen.State {
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
            globalIconEventSink = { event -> event.handleNavigationIconClicked(navigator) }
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

@AssistedFactory
@CircuitInject(AnnouncementScreen::class, SingletonComponent::class)
interface Factory {
    fun create(navigator: Navigator): AnnouncementPresenter
}
