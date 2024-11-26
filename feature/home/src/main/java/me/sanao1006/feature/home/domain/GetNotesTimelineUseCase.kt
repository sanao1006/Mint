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
    suspend operator fun invoke(timelineType: TimelineType): List<TimelineUiState> {
        val response = when (timelineType) {
            TimelineType.LOCAL -> notesRepository.getNotesLocalTimeline(
                notesTimeLineRequestBody = NotesTimeLineRequestBody(
                    i = dataStoreRepository.getAccessToken() ?: "",
                    limit = LIMIT
                )
            )

            TimelineType.SOCIAL -> notesRepository.getNotesHybridTimeline(
                notesTimeLineRequestBody = NotesTimeLineRequestBody(
                    i = dataStoreRepository.getAccessToken() ?: "",
                    limit = LIMIT
                )
            )

            TimelineType.GLOBAL -> notesRepository.getNotesGlobalTimeline(
                notesTimeLineRequestBody = NotesTimeLineRequestBody(
                    i = dataStoreRepository.getAccessToken() ?: "",
                    limit = LIMIT
                )
            )
        }
        return response.map { it.toTimelineUiState() }
    }

    companion object {
        private const val LIMIT = 20
    }
}

enum class TimelineType {
    LOCAL,
    SOCIAL,
    GLOBAL
}
