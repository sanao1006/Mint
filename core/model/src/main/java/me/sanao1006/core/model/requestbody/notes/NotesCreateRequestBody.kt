package me.sanao1006.core.model.requestbody.notes

import kotlinx.serialization.Serializable
import me.sanao1006.core.model.notes.Poll
import me.sanao1006.core.model.notes.ReactionAcceptance
import me.sanao1006.core.model.notes.Visibility

@Serializable
data class NotesCreateRequestBody(
    val text: String? = null,
    val visibility: Visibility,
    val visibleUserIds: List<String>? = null,
    val cw: String? = null,
    val localOnly: Boolean = false,
    val reactionAcceptance: ReactionAcceptance? = null,
    val noExtractMentions: Boolean = false,
    val noExtractHashtags: Boolean = false,
    val noExtractEmojis: Boolean = false,
    val replyId: String? = null,
    val renoteId: String? = null,
    val channelId: String? = null,
    val fileIds: List<String>? = null,
    val poll: Poll? = null,
    val mediaIds: List<String>? = null
)
