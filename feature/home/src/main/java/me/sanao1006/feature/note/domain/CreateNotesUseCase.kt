package me.sanao1006.feature.note.domain

import me.sanao1006.core.data.repository.NotesRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CreateNotesUseCase @Inject constructor(
    private val notesRepository: NotesRepository
) {
    suspend operator fun invoke(
        text: String,
        visibility: me.sanao1006.core.model.notes.Visibility,
        localOnly: Boolean,
        reactionAcceptance: me.sanao1006.core.model.notes.ReactionAcceptance?
    ) {
        notesRepository.createNotes(
            notesCreateRequestBody = me.sanao1006.core.model.notes.NotesCreateRequestBody(
                text = text,
                visibility = visibility,
                localOnly = localOnly,
                reactionAcceptance = reactionAcceptance
            )
        )
    }
}
