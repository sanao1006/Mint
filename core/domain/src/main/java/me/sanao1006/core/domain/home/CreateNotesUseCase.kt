package me.sanao1006.core.domain.home

import me.sanao1006.core.data.repository.NotesRepository
import me.sanao1006.core.model.requestbody.notes.NotesCreateRequestBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CreateNotesUseCase @Inject constructor(
    private val notesRepository: NotesRepository
) {
    suspend operator fun invoke(
        text: String? = null,
        cw: String? = null,
        visibility: me.sanao1006.core.model.notes.Visibility,
        localOnly: Boolean,
        reactionAcceptance: me.sanao1006.core.model.notes.ReactionAcceptance?,
        replyId: String? = null,
        renoteId: String? = null
    ) {
        notesRepository.createNotes(
            notesCreateRequestBody = NotesCreateRequestBody(
                text = text,
                cw = cw,
                visibility = visibility,
                localOnly = localOnly,
                reactionAcceptance = reactionAcceptance,
                replyId = replyId,
                renoteId = renoteId
            )
        )
    }
}
