package me.sanao1006.core.model.home.notes

data class NoteScreenUiState(
    val noteText: String,
    val visibility: Visibility = Visibility.PUBLIC,
    val localOnly: Boolean = false,
    val reactionAcceptance: ReactionAcceptance? = null,
    val isShowBottomSheet: Boolean = false,
    val noteOptionContent: NoteOptionContent = NoteOptionContent.VISIBILITY
)

enum class NoteOptionContent {
    VISIBILITY,
    LOCAL_ONLY,
    REACTION_ACCEPTANCE
}