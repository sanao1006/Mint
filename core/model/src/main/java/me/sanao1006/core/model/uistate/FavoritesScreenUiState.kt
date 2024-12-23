package me.sanao1006.core.model.uistate

import me.sanao1006.core.model.notes.TimelineItem

data class FavoritesScreenUiState(
    var timelineItems: List<TimelineItem?> = listOf(),
    val isSuccessLoading: Boolean? = null,
    var showBottomSheet: Boolean = false,
    var timelineAction: TimelineItemAction = TimelineItemAction.Renote,
    val selectedUserId: String? = null,
    val selectedNoteText: String? = null,
    val selectedNoteLink: String? = null
)
