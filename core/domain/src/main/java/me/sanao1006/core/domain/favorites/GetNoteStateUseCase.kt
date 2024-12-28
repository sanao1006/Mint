package me.sanao1006.core.domain.favorites

import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import me.sanao1006.core.data.repository.NotesRepository
import me.sanao1006.core.data.util.suspendRunCatching
import me.sanao1006.core.model.requestbody.notes.NotesNoteIdRequestBody
import me.sanao1006.core.model.response.notes.NotesStateResponse
import me.sanao1006.core.network.di.IODispatcher

class GetNoteStateUseCase @Inject constructor(
    private val notesRepository: NotesRepository,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(noteId: String): Result<NotesStateResponse> {
        return withContext(ioDispatcher) {
            suspendRunCatching {
                notesRepository.getNotesState(
                    body = NotesNoteIdRequestBody(noteId = noteId)
                )
            }
        }
    }
}
