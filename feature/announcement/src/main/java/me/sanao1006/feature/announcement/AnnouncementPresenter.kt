package me.sanao1006.feature.announcement

import androidx.compose.runtime.Composable
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.components.SingletonComponent
import me.sanao1006.core.model.uistate.AnnouncementUiState
import me.sanao1006.screens.AnnouncementScreen
import me.sanao1006.screens.event.handleNavigationIconClicked

class AnnouncementPresenter @AssistedInject constructor(
    @Assisted private val navigator: Navigator
) : Presenter<AnnouncementScreen.State> {
    @Composable
    override fun present(): AnnouncementScreen.State {
        val uiState = AnnouncementUiState.Loading

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