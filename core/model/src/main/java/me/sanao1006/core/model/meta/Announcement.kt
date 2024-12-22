package me.sanao1006.core.model.meta


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Announcement(
    @SerialName("createdAt")
    val createdAt: String,
    @SerialName("display")
    val display: String,
    @SerialName("forYou")
    val forYou: Boolean,
    @SerialName("icon")
    val icon: String,
    @SerialName("id")
    val id: String,
    @SerialName("imageUrl")
    val imageUrl: String? = null,
    @SerialName("isRead")
    val isRead: Boolean,
    @SerialName("needConfirmationToRead")
    val needConfirmationToRead: Boolean,
    @SerialName("silence")
    val silence: Boolean,
    @SerialName("text")
    val text: String,
    @SerialName("title")
    val title: String,
    @SerialName("updatedAt")
    val updatedAt: String? = null,
)
