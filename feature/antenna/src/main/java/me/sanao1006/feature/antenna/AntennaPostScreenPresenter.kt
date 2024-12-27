package me.sanao1006.feature.antenna

import androidx.compose.runtime.Composable
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.presenter.Presenter
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.components.SingletonComponent
import me.sanao1006.screens.event.AntennaPostScreen
import me.sanao1006.screens.event.AntennaPostScreenType

class AntennaPostScreenPresenter @AssistedInject constructor(
    @Assisted private val screen: AntennaPostScreen
) : Presenter<AntennaPostScreen.State> {
    @Composable
    override fun present(): AntennaPostScreen.State {
        val postScreenType = if (screen.antenna == null) {
            AntennaPostScreenType.CREATE
        } else {
            AntennaPostScreenType.EDIT
        }

        return AntennaPostScreen.State(
            screenType = postScreenType
        ) { event ->
            when (event) {
                is AntennaPostScreen.Event.OnSaveClick -> {
                    // Save the antenna
                }

                is AntennaPostScreen.Event.OnDeleteClick -> {
                    // Delete the antenna
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
