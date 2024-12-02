package me.sanao1006.core.model.notes

data class NoteScreenUiState(
    val noteText: String,
    val visibility: me.sanao1006.core.model.notes.Visibility = me.sanao1006.core.model.notes.Visibility.PUBLIC,
    val localOnly: Boolean = false,
    val reactionAcceptance: me.sanao1006.core.model.notes.ReactionAcceptance? = null,
    val isShowBottomSheet: Boolean = false,
    val noteOptionContent: me.sanao1006.core.model.notes.NoteOptionContent = me.sanao1006.core.model.notes.NoteOptionContent.VISIBILITY
)

enum class NoteOptionContent {
    VISIBILITY,
    LOCAL_ONLY,
    REACTION_ACCEPTANCE
}
