package me.sanao1006.core.model.uistate

import me.sanao1006.core.model.common.User
import me.sanao1006.core.model.notes.TimelineItem

data class NotificationUiState(
    var notificationUiStateObjects: List<NotificationUiStateObject> = emptyList(),
    var isSuccessCreateNote: Boolean? = null
)

data class NotificationUiStateObject(
    val id: String,
    val type: String,
    val user: User? = null,
    val userId: String? = null,
    val createdAt: String,
    val timelineItem: TimelineItem? = null
)
