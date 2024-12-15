package me.sanao1006.core.model.uistate

import me.sanao1006.core.model.common.User
import me.sanao1006.core.model.notes.TimelineItem

data class NotificationUiState(
    val id: String,
    val type: String,
    val user: User,
    val userId: String,
    val createdAt: String,
    val timelineItem: TimelineItem
)
