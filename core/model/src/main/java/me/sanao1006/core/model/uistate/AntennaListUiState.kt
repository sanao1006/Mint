package me.sanao1006.core.model.uistate

import me.sanao1006.core.model.notes.TimelineItem

data class AntennaListUiState(
    var timelineItems: List<TimelineItem?> = listOf()
)
