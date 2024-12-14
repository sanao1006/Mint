package me.sanao1006.core.domain.home

import javax.inject.Inject
import javax.inject.Singleton
import me.sanao1006.core.data.repository.NotesRepository
import me.sanao1006.core.model.requestbody.notes.NotesCreateRequestBody

@Singleton
class CreateNotesUseCase @Inject constructor(
    private val notesRepository: NotesRepository
) {
    suspend operator fun invoke(
        text: String? = null,
        visibility: me.sanao1006.core.model.notes.Visibility,
        localOnly: Boolean,
        reactionAcceptance: me.sanao1006.core.model.notes.ReactionAcceptance?,
        replyId: String? = null,
        renoteId: String? = null
    ) {
        notesRepository.createNotes(
            notesCreateRequestBody = NotesCreateRequestBody(
                text = text,
                visibility = visibility,
                localOnly = localOnly,
                reactionAcceptance = reactionAcceptance,
                replyId = replyId,
                renoteId = renoteId
            )
        )
    }
}
