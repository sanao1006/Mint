package me.sanao1006.core.model.home.notes

import kotlinx.serialization.Serializable

@Serializable
data class NotesCreateRequestBody(
    val text: String,
    val visibility: Visibility,
    val visibleUserIds: List<String?> = listOf(null),
    val cw: String? = null,
    val localOnly: Boolean = false,
    val reactionAcceptance: ReactionAcceptance,
    val noExtractMentions: Boolean = false,
    val noExtractHashtags: Boolean = false,
    val noExtractEmojis: Boolean = false,
    val replyId: String? = null,
    val renoteId: String? = null,
    val channelId: String? = null,
    val fileIds: List<String?> = listOf(null),
    val poll: Poll? = null,
    val mediaIds: List<String?> = listOf(null)
)
