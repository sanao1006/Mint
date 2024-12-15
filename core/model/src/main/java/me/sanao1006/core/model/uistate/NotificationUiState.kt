package me.sanao1006.core.model.uistate

import me.sanao1006.core.model.common.User
import me.sanao1006.core.model.notes.TimelineItem

sealed interface NotificationUiState {
    data object Loading : NotificationUiState
    data object Failure : NotificationUiState
    data class Success(val list: List<NotificationUiStateObject>) : NotificationUiState
}

data class NotificationUiStateObject(
    val id: String,
    val type: String,
    val user: User,
    val userId: String,
    val createdAt: String,
    val timelineItem: TimelineItem
)
