package me.sanao1006.core.model.uistate

import me.sanao1006.core.model.notes.ReactionAcceptance
import me.sanao1006.core.model.notes.Visibility

data class NoteScreenUiState(
    val noteText: String,
    val visibility: Visibility = Visibility.PUBLIC,
    val localOnly: Boolean = false,
    val reactionAcceptance: ReactionAcceptance? = null,
    val isShowBottomSheet: Boolean = false,
    val noteOptionContent: NoteOptionContent = NoteOptionContent.VISIBILITY,
    val replyId: String? = null
)

enum class NoteOptionContent {
    VISIBILITY,
    LOCAL_ONLY,
    REACTION_ACCEPTANCE
}
