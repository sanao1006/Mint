package me.sanao1006.feature.search

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuitx.effects.LaunchedImpressionEffect
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
import me.sanao1006.core.data.compositionLocal.LocalNavigator
import me.sanao1006.core.domain.home.UpdateAccountUseCase
import me.sanao1006.core.model.LoginUserInfo
import me.sanao1006.screens.SearchScreen
import me.sanao1006.screens.event.handleBottomAppBarActionEvent
import me.sanao1006.screens.event.handleDrawerEvent
import me.sanao1006.screens.event.handleNavigationIconClicked

@CircuitInject(SearchScreen::class, SingletonComponent::class)
class SearchScreenPresenter @Inject constructor(
    private val updateMyAccountUseCase: UpdateAccountUseCase
) : Presenter<SearchScreen.State> {
    @Composable
    override fun present(): SearchScreen.State {
        val navigator = LocalNavigator.current
        var loginUserInfo: LoginUserInfo by rememberRetained {
            mutableStateOf(
                LoginUserInfo()
            )
        }
        LaunchedImpressionEffect(Unit) {
            loginUserInfo = updateMyAccountUseCase()
        }
        return SearchScreen.State(
            loginUserInfo = loginUserInfo,
            drawerEventSink = { event -> event.handleDrawerEvent(navigator, loginUserInfo) },
            globalIconEventSink = { event -> event.handleNavigationIconClicked(navigator) },
            bottomAppBarActionEventSink = { event ->
                event.handleBottomAppBarActionEvent(navigator)
            },
            eventSink = { event -> }
        )
    }
}
