package me.sanao1006.core.model.uistate

import me.sanao1006.core.model.notes.TimelineItem

data class HomeScreenUiState(
    var timelineItems: List<TimelineItem?> = listOf(),
    var untilId: String = ""
)

enum class TimelineItemAction {
    Renote,
    Option,
    None
}
