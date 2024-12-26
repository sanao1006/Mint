package me.sanao1006.feature.user

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
import me.sanao1006.core.data.compositionLocal.LocalNavigator
import me.sanao1006.core.domain.user.GetUserShowUserCase
import me.sanao1006.core.model.requestbody.users.UsersShowRequestBody
import me.sanao1006.core.model.uistate.UserScreenUiState
import me.sanao1006.screens.UserScreen
import me.sanao1006.screens.event.globalIcon.GlobalIconEventPresenter

class UserScreenPresenter @AssistedInject constructor(
    @Assisted private val screen: UserScreen,
    private val getUserShowUserCase: GetUserShowUserCase,
    private val globalIconEventPresenter: GlobalIconEventPresenter
) : Presenter<UserScreen.State> {
    @Composable
    override fun present(): UserScreen.State {
        val globalIconEventState = globalIconEventPresenter.present()

        val navigator = LocalNavigator.current
        var userUiState: UserScreenUiState by rememberRetained {
            mutableStateOf(UserScreenUiState.Loading)
        }

        LaunchedImpressionEffect(Unit) {
            userUiState = getUserShowUserCase(
                isFromDrawer = screen.isFromDrawer,
                usersShowRequestBody = UsersShowRequestBody(
                    userId = screen.userId,
                    username = screen.userName,
                    host = screen.host
                )
            )
        }

        return UserScreen.State(
            globalIconEventSink = globalIconEventState.eventSink,
            uiState = userUiState
        ) { event ->
            when (event) {
                UserScreen.Event.OnNotesCountClicked -> {}
                UserScreen.Event.OnFollowersCountClicked -> {}
                UserScreen.Event.OnFollowingCountClicked -> {}
                UserScreen.Event.OnLoadingFailed -> {
                    navigator.pop()
                }
            }
        }
    }
}

@CircuitInject(UserScreen::class, SingletonComponent::class)
@AssistedFactory
fun interface Factory {
    fun create(screen: UserScreen): UserScreenPresenter
}
