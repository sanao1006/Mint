package me.sanao1006.core.domain.favorites

import javax.inject.Inject
import me.sanao1006.core.data.repository.NotesRepository
import me.sanao1006.core.data.util.suspendRunCatching
import me.sanao1006.core.model.requestbody.notes.NotesNoteIdRequestBody
import me.sanao1006.core.model.response.notes.NotesStateResponse

class GetNoteStateUseCase @Inject constructor(
    private val notesRepository: NotesRepository
) {
    suspend operator fun invoke(noteId: String): Result<NotesStateResponse> {
        return suspendRunCatching {
            notesRepository.getNotesState(
                body = NotesNoteIdRequestBody(noteId = noteId)
            )
        }
    }
}
