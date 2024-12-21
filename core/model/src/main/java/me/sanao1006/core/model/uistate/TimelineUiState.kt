package me.sanao1006.core.model.uistate

import me.sanao1006.core.model.notes.TimelineItem

data class TimelineUiState(
    var timelineItems: List<TimelineItem?> = listOf(),
    var isSuccessCreateNote: Boolean? = null,
    var showBottomSheet: Boolean = false,
    var timelineAction: TimelineItemAction = TimelineItemAction.Renote,
    val selectedUserId: String? = null,
    val selectedNoteText: String? = null,
    val selectedNoteLink: String? = null
)

enum class TimelineItemAction {
    Renote,
    Option,
    None
}
