package me.sanao1006.core.model.response.notification

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import me.sanao1006.core.model.common.User
import me.sanao1006.core.model.response.notes.NotesTimelineResponse
import me.sanao1006.core.model.uistate.NotificationUiState

@Serializable
data class NotificationResponse(
    @SerialName("createdAt")
    val createdAt: String,
    @SerialName("id")
    val id: String,
    @SerialName("note")
    val note: NotesTimelineResponse,
    @SerialName("type")
    val type: String,
    @SerialName("user")
    val user: User,
    @SerialName("userId")
    val userId: String
) {
    fun toNotificationUiState(): NotificationUiState {
        return NotificationUiState(
            id = id,
            type = type,
            user = user,
            userId = userId,
            createdAt = createdAt,
            timelineItem = note.toTimelineUiState()
        )
    }
}
