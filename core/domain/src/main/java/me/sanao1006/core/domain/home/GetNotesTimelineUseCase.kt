package me.sanao1006.core.domain.home

import javax.inject.Inject
import me.sanao1006.core.data.repository.NotesRepository
import me.sanao1006.core.model.notes.TimelineItem
import me.sanao1006.core.model.requestbody.notes.NotesTimeLineRequestBody

class GetNotesTimelineUseCase @Inject constructor(
    private val notesRepository: NotesRepository
) {
    suspend operator fun invoke(timelineType: TimelineType): List<TimelineItem> {
        return try {
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
            response.map { it.toTimelineUiState() }
        } catch (e: Exception) {
            emptyList()
        }
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
