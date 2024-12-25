package me.sanao1006.core.model.uistate

data class TimelineUiState(
    val isSuccessLoading: Boolean? = null,
    val isFavorite: Boolean = false,
    val showBottomSheet: Boolean = false,
    val timelineAction: TimelineItemAction = TimelineItemAction.Renote,
    val selectedUserId: String? = null,
    val selectedNoteText: String? = null,
    val selectedNoteLink: String? = null
)
