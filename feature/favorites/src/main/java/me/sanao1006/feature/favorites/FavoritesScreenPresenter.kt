package me.sanao1006.feature.favorites

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuitx.effects.LaunchedImpressionEffect
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.sanao1006.core.domain.favorites.GetMyFavoriteUseCase
import me.sanao1006.core.model.notes.TimelineItem
import me.sanao1006.core.model.uistate.FavoritesScreenUiState
import me.sanao1006.screens.FavoritesScreen
import me.sanao1006.screens.event.globalIcon.GlobalIconEventPresenter
import me.sanao1006.screens.event.timeline.TimelineEventPresenter

@CircuitInject(FavoritesScreen::class, SingletonComponent::class)
class FavoritesScreenPresenter @Inject constructor(
    private val getMyFavoriteUseCase: GetMyFavoriteUseCase,
    private val timelineEventPresenter: TimelineEventPresenter,
    private val globalIconEventPresenter: GlobalIconEventPresenter
) : Presenter<FavoritesScreen.State> {
    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    override fun present(): FavoritesScreen.State {
        val timelineEventState = timelineEventPresenter.present()
        val globalIconEventState = globalIconEventPresenter.present()

        var isRefreshed by remember { mutableStateOf(false) }
        var favoritesScreenUiState by rememberRetained {
            mutableStateOf(FavoritesScreenUiState())
        }
        val scope = rememberCoroutineScope()

        val pullRefreshState = rememberPullRefreshState(
            refreshing = isRefreshed,
            onRefresh = {
                scope.launch {
                    isRefreshed = true
                    favoritesScreenUiState = fetchFavoriteItems(
                        uiState = favoritesScreenUiState,
                        getItems = { getMyFavoriteUseCase.invoke().timelineItems },
                        setSuccessLoading = { timelineEventState.setSuccessLoading(it) }
                    )
                    delay(1000L)
                    isRefreshed = false
                }
            },
            refreshThreshold = 50.dp,
            refreshingOffset = 50.dp
        )

        LaunchedImpressionEffect(Unit) {
            favoritesScreenUiState = fetchFavoriteItems(
                uiState = favoritesScreenUiState,
                getItems = { getMyFavoriteUseCase.invoke().timelineItems },
                setSuccessLoading = { timelineEventState.setSuccessLoading(it) }
            )
        }

        return FavoritesScreen.State(
            favoritesScreenUiState = favoritesScreenUiState,
            timelineUiState = timelineEventState.uiState,
            pullToRefreshState = pullRefreshState,
            timelineEventSink = timelineEventState.eventSink,
            globalIconEventSink = globalIconEventState.eventSink
        ) { event ->
            when (event) {
                FavoritesScreen.Event.OnDismissRequest -> {
                    timelineEventState.setShowBottomSheet(false)
                }
            }
        }
    }
}

private suspend fun fetchFavoriteItems(
    uiState: FavoritesScreenUiState,
    getItems: suspend () -> List<TimelineItem?>,
    setSuccessLoading: (Boolean) -> Unit
): FavoritesScreenUiState {
    val timelineItems: List<TimelineItem?> = getItems()
    return if (timelineItems.isEmpty()) {
        setSuccessLoading(false)
        uiState.copy(timelineItems = emptyList())
    } else {
        setSuccessLoading(true)
        uiState.copy(timelineItems = timelineItems)
    }
}
