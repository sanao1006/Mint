package me.sanao1006.screens.uiState

import me.sanao1006.core.model.notes.TimelineItem
import me.sanao1006.screens.event.TimelineItemEvent

data class TimelineUiState(
    var timelineItems: List<TimelineItem?> = listOf(),
    var isSuccessCreateNote: Boolean? = null,
    var showBottomSheet: Boolean = false,
    val timelineEventSink: (TimelineItemEvent) -> Unit
)
