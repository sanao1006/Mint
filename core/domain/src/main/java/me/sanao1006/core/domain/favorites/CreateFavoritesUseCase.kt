package me.sanao1006.core.domain.favorites

import javax.inject.Inject
import me.sanao1006.core.data.repository.NotesRepository
import me.sanao1006.core.data.util.suspendRunCatching
import me.sanao1006.core.model.requestbody.notes.NotesNoteIdRequestBody

class CreateFavoritesUseCase @Inject constructor(
    private val notesRepository: NotesRepository
) {
    suspend operator fun invoke(noteId: String): Result<Unit> {
        return suspendRunCatching {
            notesRepository.createNotesFavorites(
                body = NotesNoteIdRequestBody(noteId = noteId)
            )
        }
    }
}
