package me.sanao1006.core.model.notes

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import me.sanao1006.core.model.meta.Note

@Serializable
data class Channel(
    @SerialName("allowRenoteToExternal")
    val allowRenoteToExternal: Boolean = false,
    @SerialName("color")
    val color: String = "",
    @SerialName("id")
    val id: String = "",
    @SerialName("isSensitive")
    val isSensitive: Boolean = false,
    @SerialName("name")
    val name: String = "",
    @SerialName("bannerUrl")
    val bannerUrl: String? = null,
    @SerialName("createdAt")
    val createdAt: String = "",
    @SerialName("description")
    val description: String? = null,
    @SerialName("isArchived")
    val isArchived: Boolean = false,
    @SerialName("isFavorited")
    val isFavorited: Boolean = false,
    @SerialName("isFollowing")
    val isFollowing: Boolean = false,
    @SerialName("lastNotedAt")
    val lastNotedAt: String? = null,
    @SerialName("notesCount")
    val notesCount: Int = 0,
    @SerialName("pinnedNoteIds")
    val pinnedNoteIds: List<String> = emptyList(),
    @SerialName("pinnedNotes")
    val pinnedNotes: List<Note> = emptyList(),
    @SerialName("userId")
    val userId: String? = null,
    @SerialName("usersCount")
    val usersCount: Int = 0
)
