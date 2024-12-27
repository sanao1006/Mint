package me.sanao1006.feature.antenna

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
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.sanao1006.core.model.uistate.AntennaListUiState
import me.sanao1006.screens.AntennaListScreen
import me.sanao1006.screens.event.globalIcon.GlobalIconEventPresenter
import me.sanao1006.screens.event.timeline.TimelineEventPresenter

class AntennaListScreenPresenter @AssistedInject constructor(
    @Assisted private val screen: AntennaListScreen,
    private val timelineEventPresenter: TimelineEventPresenter,
    private val globalIconEventPresenter: GlobalIconEventPresenter
) : Presenter<AntennaListScreen.State> {
    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    override fun present(): AntennaListScreen.State {
        val timelineEventState = timelineEventPresenter.present()
        val globalEventState = globalIconEventPresenter.present()

        val scope = rememberCoroutineScope()
        var isRefreshed by remember { mutableStateOf(false) }
        val pullRefreshState = rememberPullRefreshState(
            refreshing = isRefreshed,
            onRefresh = {
                scope.launch {
                    isRefreshed = true
                    delay(1000L)
                    isRefreshed = false
                }
            },
            refreshThreshold = 50.dp,
            refreshingOffset = 50.dp
        )

        var uiState: AntennaListUiState by rememberRetained {
            mutableStateOf(AntennaListUiState())
        }

        return AntennaListScreen.State(
            uiState = uiState,
            timelineUiState = timelineEventState.uiState,
            globalIconEventSink = globalEventState.eventSink,
            timelineEventSink = timelineEventState.eventSink,
            pullToRefreshState = pullRefreshState,
            isRefreshed = isRefreshed
        )
    }

    @AssistedFactory
    @CircuitInject(AntennaListScreen::class, SingletonComponent::class)
    fun interface Factory {
        fun create(screen: AntennaListScreen): AntennaListScreenPresenter
    }
}
