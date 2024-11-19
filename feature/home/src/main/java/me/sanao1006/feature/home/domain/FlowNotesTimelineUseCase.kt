package me.sanao1006.feature.home.domain

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import me.sanao1006.core.data.repository.NotesRepository
import me.sanao1006.core.model.home.notes.NotesTimeLineRequestBody
import me.sanao1006.core.model.home.notes.TimelineUiState
import me.sanao1006.datastore.DataStoreRepository
import javax.inject.Inject

class FlowNotesTimelineUseCase @Inject constructor(
    private val notesRepository: NotesRepository,
    private val dataStoreRepository: DataStoreRepository
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(): Flow<List<TimelineUiState>> {
        return dataStoreRepository.flowAccessToken().flatMapLatest {
            notesRepository.getNotesTimeline(
                notesTimeLineRequestBody = NotesTimeLineRequestBody(i = it)
            )
        }.map { notesTimeline ->
            notesTimeline.map { it.toTimelineUiState() }
        }
    }
}