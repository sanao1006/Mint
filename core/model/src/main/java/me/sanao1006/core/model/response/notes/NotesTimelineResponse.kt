package me.sanao1006.core.model.response.notes

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import me.sanao1006.core.model.common.User
import me.sanao1006.core.model.notes.Channel
import me.sanao1006.core.model.notes.File
import me.sanao1006.core.model.notes.Poll
import me.sanao1006.core.model.notes.Reactions
import me.sanao1006.core.model.notes.Renote
import me.sanao1006.core.model.notes.Reply
import me.sanao1006.core.model.notes.TimelineItem
import me.sanao1006.core.model.notes.Visibility

@Serializable
data class NotesTimelineResponse(
    @SerialName("channel")
    val channel: Channel = Channel(),
    @SerialName("channelId")
    val channelId: String = "",
    @SerialName("clippedCount")
    val clippedCount: Int = 0,
    @SerialName("createdAt")
    val createdAt: String = "",
    @SerialName("cw")
    val cw: String? = null,
    @SerialName("deletedAt")
    val deletedAt: String = "",
    @SerialName("fileIds")
    val fileIds: List<String> = listOf(),
    @SerialName("files")
    val files: List<File> = listOf(),
    @SerialName("id")
    val id: String = "",
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
    @SerialName("reactions")
    val reactions: Reactions? = null,
    @SerialName("renote")
    val renote: Renote? = Renote(),
    @SerialName("renoteCount")
    val renoteCount: Int = 0,
    @SerialName("renoteId")
    val renoteId: String? = null,
    @SerialName("repliesCount")
    val repliesCount: Int = 0,
    @SerialName("reply")
    val reply: Reply? = Reply(),
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
) {
    fun toTimelineUiState(): TimelineItem {
        return TimelineItem(
            user = user,
            text = text.orEmpty(),
            id = id,
            visibility = Visibility.get(visibility),
            uri = uri,
            createdAt = createdAt
        )
    }
}
