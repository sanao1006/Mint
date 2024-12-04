package me.sanao1006.feature.home.domain

import me.sanao1006.core.data.repository.NotesRepository
import me.sanao1006.core.model.notes.NotesTimeLineRequestBody
import me.sanao1006.core.model.notes.TimelineUiState
import javax.inject.Inject

class GetNotesTimelineUseCase @Inject constructor(
    private val notesRepository: NotesRepository
) {
    suspend operator fun invoke(timelineType: TimelineType): List<TimelineUiState> {
        val response = when (timelineType) {
            TimelineType.LOCAL -> notesRepository.getNotesLocalTimeline(
                notesTimeLineRequestBody = NotesTimeLineRequestBody(
                    limit = LIMIT
                )
            )

            TimelineType.SOCIAL -> notesRepository.getNotesHybridTimeline(
                notesTimeLineRequestBody = NotesTimeLineRequestBody(
                    limit = LIMIT
                )
            )

            TimelineType.GLOBAL -> notesRepository.getNotesGlobalTimeline(
                notesTimeLineRequestBody = NotesTimeLineRequestBody(
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
