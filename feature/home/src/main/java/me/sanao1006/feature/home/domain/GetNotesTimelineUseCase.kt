package me.sanao1006.feature.home.domain

import me.sanao1006.core.data.repository.NotesRepository
import me.sanao1006.core.model.home.notes.NotesTimeLineRequestBody
import me.sanao1006.core.model.home.notes.TimelineUiState
import me.sanao1006.datastore.DataStoreRepository
import javax.inject.Inject

class GetNotesTimelineUseCase @Inject constructor(
    private val notesRepository: NotesRepository,
    private val dataStoreRepository: DataStoreRepository
) {
    suspend operator fun invoke(): List<TimelineUiState> {
        val response = notesRepository.getNotesTimeline(
            notesTimeLineRequestBody = NotesTimeLineRequestBody(
                i = dataStoreRepository.getAccessToken() ?: "",
                limit = 20
            )
        )
        return response.map { it.toTimelineUiState() }
    }
}
