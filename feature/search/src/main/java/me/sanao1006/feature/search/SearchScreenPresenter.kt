package me.sanao1006.feature.search

import androidx.compose.runtime.Composable
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.presenter.Presenter
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
import me.sanao1006.core.data.compositionLocal.LocalNavigator
import me.sanao1006.screens.SearchScreen
import me.sanao1006.screens.event.handleNavigationIconClicked

@CircuitInject(SearchScreen::class, SingletonComponent::class)
class SearchScreenPresenter @Inject constructor() : Presenter<SearchScreen.State> {
    @Composable
    override fun present(): SearchScreen.State {
        val navigator = LocalNavigator.current
        return SearchScreen.State(
            globalIconEventSink = { event -> event.handleNavigationIconClicked(navigator) },
            eventSink = { event -> }
        )
    }
}
