package me.sanao1006.feature.search

import androidx.compose.runtime.Composable
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.presenter.Presenter
import dagger.hilt.components.SingletonComponent
import me.sanao1006.screens.SearchScreen
import me.sanao1006.screens.event.GlobalIconEventPresenter
import javax.inject.Inject

@CircuitInject(SearchScreen::class, SingletonComponent::class)
class SearchScreenPresenter @Inject constructor(
    private val globalIconEventPresenter: GlobalIconEventPresenter
) : Presenter<SearchScreen.State> {
    @Composable
    override fun present(): SearchScreen.State {
        val globalIconEventState = globalIconEventPresenter.present()
        return SearchScreen.State(
            globalIconEventSink = globalIconEventState.eventSink,
            eventSink = { event -> }
        )
    }
}
