package me.sanao1006.feature.user

import androidx.compose.runtime.Composable
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.components.SingletonComponent
import me.sanao1006.screens.UserScreen

class UserScreenPresenter @AssistedInject constructor(
    @Assisted private val navigator: Navigator
) : Presenter<UserScreen.State> {
    @Composable
    override fun present(): UserScreen.State {
        return UserScreen.State { event ->
            when (event) {
                UserScreen.Event.OnNavigationIconClicked -> {
                    navigator.pop()
                }

                UserScreen.Event.OnNotesCountClicked -> {}
                UserScreen.Event.OnFollowersCountClicked -> {}
                UserScreen.Event.OnFollowingCountClicked -> {}
            }
        }
    }
}

@CircuitInject(UserScreen::class, SingletonComponent::class)
@AssistedFactory
interface Factory {
    fun create(navigator: Navigator): UserScreenPresenter
}
