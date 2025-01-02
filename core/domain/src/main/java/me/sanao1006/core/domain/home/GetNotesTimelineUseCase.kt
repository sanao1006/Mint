package me.sanao1006.core.domain.home

import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import me.sanao1006.core.data.repository.NotesRepository
import me.sanao1006.core.model.notes.TimelineItem
import me.sanao1006.core.model.requestbody.notes.NotesTimeLineRequestBody
import me.sanao1006.core.network.di.IODispatcher

class GetNotesTimelineUseCase @Inject constructor(
    private val notesRepository: NotesRepository,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(
        timelineType: TimelineType,
        untilId: String? = null
    ): List<TimelineItem> {
        return withContext(ioDispatcher) {
            try {
                val response = when (timelineType) {
                    TimelineType.HOME -> notesRepository.getNotesHomeTimeline(
                        notesTimeLineRequestBody = NotesTimeLineRequestBody(
                            limit = LIMIT,
                            untilId = untilId ?: ""
                        )
                    )

                    TimelineType.SOCIAL -> notesRepository.getNotesHybridTimeline(
                        notesTimeLineRequestBody = NotesTimeLineRequestBody(
                            limit = LIMIT,
                            untilId = untilId ?: ""
                        )
                    )

                    TimelineType.GLOBAL -> notesRepository.getNotesGlobalTimeline(
                        notesTimeLineRequestBody = NotesTimeLineRequestBody(
                            limit = LIMIT,
                            untilId = untilId ?: ""
                        )
                    )
                }
                response.map { it.toTimelineUiState() }
            } catch (e: Exception) {
                emptyList()
            }
        }
    }

    companion object {
        private const val LIMIT = 20
    }
}

enum class TimelineType {
    HOME,
    SOCIAL,
    GLOBAL
}
