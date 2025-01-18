package me.sanao1006.core.model.uistate

import me.sanao1006.core.model.notes.Instance
import me.sanao1006.core.model.notes.ReactionAcceptance
import me.sanao1006.core.model.notes.Visibility

data class NoteScreenUiState(
    val noteText: String,
    val visibility: Visibility = Visibility.PUBLIC,
    val localOnly: Boolean = false,
    val reactionAcceptance: ReactionAcceptance? = null,
    val isShowBottomSheet: Boolean = false,
    val noteOptionContent: NoteOptionContent = NoteOptionContent.VISIBILITY,
    val replyId: String? = null,
    val renoteId: String? = null,
    val noteTarget: NoteTargetState? = null,
    val cw: String? = null,
    val expandCw: Boolean = false
)

enum class NoteOptionContent {
    VISIBILITY,
    LOCAL_ONLY,
    REACTION_ACCEPTANCE
}

// reply or quote
data class NoteTargetState(
    val userName: String,
    val name: String?,
    val avatarUrl: String?,
    val instance: Instance?,
    val text: String,
    val host: String?
)
