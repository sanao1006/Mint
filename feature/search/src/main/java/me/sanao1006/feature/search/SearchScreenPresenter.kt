package me.sanao1006.feature.search

import androidx.compose.runtime.Composable
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.launch
import me.sanao1006.screens.SearchScreen

class SearchScreenPresenter @AssistedInject constructor(
    @Assisted private val navigator: Navigator
) : Presenter<SearchScreen.State> {
    @Composable
    override fun present(): SearchScreen.State {
        return SearchScreen.State(
            query = "",
            eventSink = { event ->
                when (event) {
                    is SearchScreen.Event.OnNavigationIconClicked -> {
                        event.scope.launch {
                            event.drawerState.apply {
                                if (isClosed) open() else close()
                            }
                        }
                    }
                }
            }
        )
    }
}

@CircuitInject(SearchScreen::class, SingletonComponent::class)
@AssistedFactory
interface Factory {
    fun create(navigator: Navigator): SearchScreenPresenter
}
