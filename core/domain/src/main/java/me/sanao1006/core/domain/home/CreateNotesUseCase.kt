package me.sanao1006.core.domain.home

import javax.inject.Inject
import javax.inject.Singleton
import me.sanao1006.core.data.repository.NotesRepository

@Singleton
class CreateNotesUseCase @Inject constructor(
    private val notesRepository: NotesRepository
) {
    suspend operator fun invoke(
        text: String,
        visibility: me.sanao1006.core.model.notes.Visibility,
        localOnly: Boolean,
        reactionAcceptance: me.sanao1006.core.model.notes.ReactionAcceptance?,
        replyId: String? = null
    ) {
        notesRepository.createNotes(
            notesCreateRequestBody = me.sanao1006.core.model.notes.NotesCreateRequestBody(
                text = text,
                visibility = visibility,
                localOnly = localOnly,
                reactionAcceptance = reactionAcceptance,
                replyId = replyId
            )
        )
    }
}
