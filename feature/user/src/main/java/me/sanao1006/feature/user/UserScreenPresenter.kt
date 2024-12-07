package me.sanao1006.feature.user

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
import me.sanao1006.core.domain.user.GetUserShowUserCase
import me.sanao1006.core.model.user.UserScreenUiState
import me.sanao1006.core.model.user.UsersShowRequestBody
import me.sanao1006.screens.UserScreen

class UserScreenPresenter @AssistedInject constructor(
    @Assisted private val navigator: Navigator,
    @Assisted private val screen: UserScreen,
    private val getUserShowUserCase: GetUserShowUserCase
) : Presenter<UserScreen.State> {
    @Composable
    override fun present(): UserScreen.State {
        var userUiState: UserScreenUiState by rememberRetained {
            mutableStateOf(UserScreenUiState())
        }

        LaunchedImpressionEffect(Unit) {
            userUiState = getUserShowUserCase(
                UsersShowRequestBody(
                    userId = screen.userId,
                    username = screen.userName,
                    host = screen.host
                )
            )
        }
        return UserScreen.State(
            uiState = userUiState
        ) { event ->
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
    fun create(
        navigator: Navigator,
        screen: UserScreen
    ): UserScreenPresenter
}
