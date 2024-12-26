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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.sanao1006.core.data.compositionLocal.LocalNavigator
import me.sanao1006.core.domain.favorites.GetMyFavoriteUseCase
import me.sanao1006.core.model.uistate.FavoritesScreenUiState
import me.sanao1006.screens.FavoritesScreen
import me.sanao1006.screens.event.GlobalIconEventPresenter
import me.sanao1006.screens.event.TimelineEventPresenter
import javax.inject.Inject

@CircuitInject(FavoritesScreen::class, SingletonComponent::class)
class FavoritesScreenPresenter @Inject constructor(
    private val getMyFavoriteUseCase: GetMyFavoriteUseCase,
    private val timelineEventPresenter: TimelineEventPresenter,
    private val globalIconEventPresenter: GlobalIconEventPresenter
) : Presenter<FavoritesScreen.State> {
    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    override fun present(): FavoritesScreen.State {
        val navigator = LocalNavigator.current
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
                    val favorites = getMyFavoriteUseCase.invoke()
                    if (favorites.timelineItems.isEmpty()) {
                        favoritesScreenUiState = favoritesScreenUiState.copy(
                            timelineItems = emptyList()
                        )
                        timelineEventState.setSuccessLoading(false)
                    } else {
                        favoritesScreenUiState = favoritesScreenUiState.copy(
                            timelineItems = favorites.timelineItems
                        )
                        timelineEventState.setSuccessLoading(true)
                    }
                    delay(1000L)
                    isRefreshed = false
                }
            },
            refreshThreshold = 50.dp,
            refreshingOffset = 50.dp
        )

        LaunchedImpressionEffect(Unit) {
            val favorites = getMyFavoriteUseCase.invoke()
            if (favorites.timelineItems.isEmpty()) {
                favoritesScreenUiState = favoritesScreenUiState.copy(
                    timelineItems = emptyList()
                )
                timelineEventState.setSuccessLoading(false)
            } else {
                favoritesScreenUiState = favoritesScreenUiState.copy(
                    timelineItems = favorites.timelineItems
                )
                timelineEventState.setSuccessLoading(true)
            }
        }

        return FavoritesScreen.State(
            navigator = navigator,
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
