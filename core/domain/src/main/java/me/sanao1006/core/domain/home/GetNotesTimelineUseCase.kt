package me.sanao1006.core.domain.home

import javax.inject.Inject
import me.sanao1006.core.data.repository.NotesRepository
import me.sanao1006.core.model.notes.NotesTimeLineRequestBody
import me.sanao1006.core.model.notes.TimelineItem

class GetNotesTimelineUseCase @Inject constructor(
    private val notesRepository: NotesRepository
) {
    suspend operator fun invoke(timelineType: TimelineType): List<TimelineItem> {
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
