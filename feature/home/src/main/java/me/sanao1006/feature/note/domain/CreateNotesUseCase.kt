package me.sanao1006.feature.note.domain

import me.sanao1006.core.data.repository.NotesRepository
import me.sanao1006.core.model.home.notes.NotesCreateRequestBody
import me.sanao1006.core.model.home.notes.ReactionAcceptance
import me.sanao1006.core.model.home.notes.Visibility
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CreateNotesUseCase @Inject constructor(
    private val notesRepository: NotesRepository
) {
    suspend operator fun invoke(
        text: String,
        visibility: Visibility,
        reactionAcceptance: ReactionAcceptance? = null
    ) {
        notesRepository.createNotes(
            notesCreateRequestBody = NotesCreateRequestBody(
                text = text,
                visibility = visibility,
                reactionAcceptance = reactionAcceptance
            )
        )
    }
}
