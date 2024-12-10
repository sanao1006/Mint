package me.sanao1006.feature.search

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
import me.sanao1006.core.domain.home.UpdateAccountUseCase
import me.sanao1006.core.model.LoginUserInfo
import me.sanao1006.screens.SearchScreen
import me.sanao1006.screens.event.handleDrawerEvent
import me.sanao1006.screens.event.handleNavigationIconClicked

class SearchScreenPresenter @AssistedInject constructor(
    @Assisted private val navigator: Navigator,
    private val updateMyAccountUseCase: UpdateAccountUseCase
) : Presenter<SearchScreen.State> {
    @Composable
    override fun present(): SearchScreen.State {
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
            globalIconEventSink = { event -> event.handleNavigationIconClicked() },
            eventSink = { event -> }
        )
    }
}

@CircuitInject(SearchScreen::class, SingletonComponent::class)
@AssistedFactory
interface Factory {
    fun create(navigator: Navigator): SearchScreenPresenter
}
