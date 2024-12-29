package me.sanao1006.core.model.response.account

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import me.sanao1006.core.model.meta.Note

@Serializable
data class IFavoritesResponse(
    @SerialName("createdAt")
    val createdAt: String,
    @SerialName("id")
    val id: String,
    @SerialName("note")
    val note: Note,
    @SerialName("noteId")
    val noteId: String
)
