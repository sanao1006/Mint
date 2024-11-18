package me.sanao1006.feature.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.presenter.Presenter
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import me.sanao1006.core.data.repository.NotesRepository
import me.sanao1006.core.model.home.notes.NotesTimeLineRequestBody
import me.sanao1006.datastore.DataStoreRepository
import javax.inject.Inject

@CircuitInject(HomeScreen::class, SingletonComponent::class)
class HomeScreenPresenter @Inject constructor(
    private val notesRepository: NotesRepository,
    private val dataStoreRepository: DataStoreRepository
) : Presenter<HomeScreen.State> {
    @OptIn(ExperimentalCoroutinesApi::class)

    private val _timelineUiState = dataStoreRepository.flowAccessToken().flatMapLatest {
        notesRepository.getNotesTimeline(
            notesTimeLineRequestBody = NotesTimeLineRequestBody(i = it)
        )
    }.map { notesTimeline ->
        notesTimeline.map { it.toTimelineUiState() }
    }

    @Composable
    override fun present(): HomeScreen.State {
        val timelineUiState = _timelineUiState.collectAsState(initial = emptyList()).value
        return HomeScreen.State(
            uiState = timelineUiState,
            eventSink = {}
        )
    }
}
