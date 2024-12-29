package me.sanao1006.core.model.response.notification

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import me.sanao1006.core.model.common.User
import me.sanao1006.core.model.meta.Note
import me.sanao1006.core.model.uistate.NotificationUiStateObject

@Serializable
data class NotificationResponse(
    @SerialName("createdAt")
    val createdAt: String,
    @SerialName("id")
    val id: String,
    @SerialName("note")
    val note: Note? = null,
    @SerialName("type")
    val type: String,
    @SerialName("user")
    val user: User? = null,
    @SerialName("userId")
    val userId: String? = null
) {
    fun toNotificationUiState(): NotificationUiStateObject =
        NotificationUiStateObject(
            id = id,
            type = type,
            user = user,
            userId = userId,
            createdAt = createdAt,
            timelineItem = note?.toTimelineUiState()
        )
}
