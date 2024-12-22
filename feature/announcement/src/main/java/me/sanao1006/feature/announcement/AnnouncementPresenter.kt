package me.sanao1006.feature.announcement

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
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
        var uiState: AnnouncementUiState by rememberRetained {
            mutableStateOf(AnnouncementUiState.Loading)
        }

        LaunchedImpressionEffect(Unit) {
            uiState = getAnnouncementsUseCase()
        }

        return AnnouncementScreen.State(
            uiState = uiState,
            globalIconEventSink = { event -> event.handleNavigationIconClicked(navigator) }
        )
    }
}

@AssistedFactory
@CircuitInject(AnnouncementScreen::class, SingletonComponent::class)
interface Factory {
    fun create(navigator: Navigator): AnnouncementPresenter
}
