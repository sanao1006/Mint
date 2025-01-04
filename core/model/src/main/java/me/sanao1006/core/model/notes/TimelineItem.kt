package me.sanao1006.core.model.notes

import kotlinx.serialization.json.JsonObject
import me.sanao1006.core.model.common.User
import me.sanao1006.core.model.meta.Note

data class TimelineItem(
    val user: User? = null,
    val text: String,
    val id: String,
    val visibility: Visibility,
    val uri: String,
    val createdAt: String,
    val reactions: JsonObject? = null,
    val reactionsEmojis: JsonObject? = null,
    val renote: Note? = null,
    val renoteCount: Int = 0,
    val repliesCount: Int = 0,
    val reply: Note? = null
)
