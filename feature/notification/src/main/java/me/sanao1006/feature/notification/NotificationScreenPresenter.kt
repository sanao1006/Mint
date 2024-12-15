package me.sanao1006.feature.notification

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.foundation.rememberAnsweringNavigator
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuitx.effects.LaunchedImpressionEffect
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.components.SingletonComponent
import me.sanao1006.core.domain.home.UpdateAccountUseCase
import me.sanao1006.core.domain.notification.GetNotificationsUseCase
import me.sanao1006.core.model.LoginUserInfo
import me.sanao1006.screens.NoteScreen
import me.sanao1006.screens.NotificationScreen
import me.sanao1006.screens.event.handleBottomAppBarActionEvent
import me.sanao1006.screens.event.handleDrawerEvent
import me.sanao1006.screens.event.handleNavigationIconClicked
import me.sanao1006.screens.event.handleNoteCreateEvent
import timber.log.Timber

class NotificationScreenPresenter @AssistedInject constructor(
    @Assisted private val navigator: Navigator,
    private val updateMyAccountUseCase: UpdateAccountUseCase,
    private val getNotificationsUseCase: GetNotificationsUseCase
) : Presenter<NotificationScreen.State> {
    @Composable
    override fun present(): NotificationScreen.State {
        var isSuccessCreateNote: Boolean? by rememberRetained { mutableStateOf(null) }
        var loginUserInfo: LoginUserInfo by rememberRetained {
            mutableStateOf(
                LoginUserInfo()
            )
        }
        val context = LocalContext.current
        val nav = rememberAnsweringNavigator<NoteScreen.Result>(navigator) { result ->
            isSuccessCreateNote = result.success
        }
        LaunchedImpressionEffect(Unit) {
            loginUserInfo = updateMyAccountUseCase()
            Timber.tag("ray").d("notifi ${getNotificationsUseCase()}")
        }
        return NotificationScreen.State(
            isSuccessCreateNote = isSuccessCreateNote,
            navigator = navigator,
            drawerUserInfo = loginUserInfo,
            noteCreateEventSink = { event ->
                event.handleNoteCreateEvent(
                    isSuccessCreateNote,
                    context,
                    navigator
                )
            },
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
