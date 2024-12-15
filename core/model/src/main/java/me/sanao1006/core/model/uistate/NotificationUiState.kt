package me.sanao1006.core.model.uistate

import me.sanao1006.core.model.common.User
import me.sanao1006.core.model.notes.TimelineItem

data class NotificationUiState(
    var notificationUiStateObjects: List<NotificationUiStateObject> = emptyList(),
    var isSuccessLoading: Boolean? = null,
    var isSuccessCreateNote: Boolean? = null,
    var showBottomSheet: Boolean = false,
    var timelineAction: TimelineItemAction = TimelineItemAction.Renote,
    val selectedUserId: String? = null,
    val selectedNoteText: String? = null,
    val selectedNoteLink: String? = null
)

data class NotificationUiStateObject(
    val id: String,
    val type: String,
    val user: User,
    val userId: String,
    val createdAt: String,
    val timelineItem: TimelineItem
)
