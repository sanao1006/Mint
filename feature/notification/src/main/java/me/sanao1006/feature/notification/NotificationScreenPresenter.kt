package me.sanao1006.feature.notification

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.components.SingletonComponent
import me.sanao1006.core.model.LoginUserInfo
import me.sanao1006.screens.NotificationScreen
import me.sanao1006.screens.event.handleBottomAppBarActionEvent
import me.sanao1006.screens.event.handleDrawerEvent
import me.sanao1006.screens.event.handleNavigationIconClicked

class NotificationScreenPresenter @AssistedInject constructor(
    @Assisted private val navigator: Navigator,
) : Presenter<NotificationScreen.State> {
    @Composable
    override fun present(): NotificationScreen.State {
        var loginUserInfo: LoginUserInfo by rememberRetained {
            mutableStateOf(
                LoginUserInfo()
            )
        }
        return NotificationScreen.State(
            navigator = navigator,
            drawerUserInfo = LoginUserInfo(),
            drawerEventSink = { event -> event.handleDrawerEvent(navigator, loginUserInfo) },
            globalIconEventSink = { event -> event.handleNavigationIconClicked(navigator) },
            bottomAppBarEventSInk = { event -> event.handleBottomAppBarActionEvent(navigator) },
            eventSink = { }
        )
    }
}

@AssistedFactory
@CircuitInject(NotificationScreen::class, SingletonComponent::class)
interface Factory {
    fun create(navigator: Navigator): NotificationScreenPresenter
}
