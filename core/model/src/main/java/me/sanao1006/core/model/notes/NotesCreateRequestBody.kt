package me.sanao1006.core.model.notes

import kotlinx.serialization.Serializable

@Serializable
data class NotesCreateRequestBody(
    val text: String,
    val visibility: me.sanao1006.core.model.notes.Visibility,
    val visibleUserIds: List<String>? = null,
    val cw: String? = null,
    val localOnly: Boolean = false,
    val reactionAcceptance: me.sanao1006.core.model.notes.ReactionAcceptance? = null,
    val noExtractMentions: Boolean = false,
    val noExtractHashtags: Boolean = false,
    val noExtractEmojis: Boolean = false,
    val replyId: String? = null,
    val renoteId: String? = null,
    val channelId: String? = null,
    val fileIds: List<String>? = null,
    val poll: me.sanao1006.core.model.notes.Poll? = null,
    val mediaIds: List<String>? = null
)
