package me.sanao1006.core.model.notes

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject
import me.sanao1006.core.model.common.User

@Serializable
data class Renote(
    @SerialName("channel")
    val channel: Channel = Channel(),
    @SerialName("channelId")
    val channelId: String? = null,
    @SerialName("clippedCount")
    val clippedCount: Int = 0,
    @SerialName("createdAt")
    val createdAt: String = "",
    @SerialName("cw")
    val cw: String? = null,
    @SerialName("deletedAt")
    val deletedAt: String? = null,
    @SerialName("emojis")
    val emojis: JsonObject? = null,
    @SerialName("fileIds")
    val fileIds: List<String> = listOf(),
    @SerialName("files")
    val files: List<File> = listOf(),
    @SerialName("id")
    val id: String,
    @SerialName("isHidden")
    val isHidden: Boolean = false,
    @SerialName("localOnly")
    val localOnly: Boolean = false,
    @SerialName("mentions")
    val mentions: List<String> = listOf(),
    @SerialName("myReaction")
    val myReaction: String? = null,
    @SerialName("poll")
    val poll: Poll? = null,
    @SerialName("reactionAcceptance")
    val reactionAcceptance: String? = null,
    @SerialName("reactionAndUserPairCache")
    val reactionAndUserPairCache: List<String> = listOf(),
    @SerialName("reactionCount")
    val reactionCount: Int = 0,
    @SerialName("reactionEmojis")
    val reactionEmojis: JsonObject? = null,
    @SerialName("reactions")
    val reactions: JsonObject? = null,
    @SerialName("renote")
    val renote: Renote? = null,
    @SerialName("renoteCount")
    val renoteCount: Int = 0,
    @SerialName("renoteId")
    val renoteId: String? = null,
    @SerialName("repliesCount")
    val repliesCount: Int = 0,
    @SerialName("reply")
    val reply: Reply? = null,
    @SerialName("replyId")
    val replyId: String? = null,
    @SerialName("tags")
    val tags: List<String> = listOf(),
    @SerialName("text")
    val text: String? = null,
    @SerialName("uri")
    val uri: String = "",
    @SerialName("url")
    val url: String = "",
    @SerialName("user")
    val user: User? = null,
    @SerialName("userId")
    val userId: String = "",
    @SerialName("visibility")
    val visibility: String = "",
    @SerialName("visibleUserIds")
    val visibleUserIds: List<String> = listOf()
)
