package me.sanao1006.core.model.notes


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NotesTimeline(
    @SerialName("channel")
    val channel: me.sanao1006.core.model.notes.Channel = me.sanao1006.core.model.notes.Channel(),
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
    val files: List<me.sanao1006.core.model.notes.File> = listOf(),
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
    val poll: me.sanao1006.core.model.notes.Poll? = me.sanao1006.core.model.notes.Poll(),
    @SerialName("reactionAcceptance")
    val reactionAcceptance: String? = null,
    @SerialName("reactionAndUserPairCache")
    val reactionAndUserPairCache: List<String> = listOf(),
    @SerialName("reactions")
    val reactions: me.sanao1006.core.model.notes.Reactions? = me.sanao1006.core.model.notes.Reactions(),
    @SerialName("renote")
    val renote: me.sanao1006.core.model.notes.Renote? = me.sanao1006.core.model.notes.Renote(),
    @SerialName("renoteCount")
    val renoteCount: Int = 0,
    @SerialName("renoteId")
    val renoteId: String? = null,
    @SerialName("repliesCount")
    val repliesCount: Int = 0,
    @SerialName("reply")
    val reply: me.sanao1006.core.model.notes.Reply? = me.sanao1006.core.model.notes.Reply(),
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
    val user: me.sanao1006.core.model.notes.User? = me.sanao1006.core.model.notes.User(),
    @SerialName("userId")
    val userId: String = "",
    @SerialName("visibility")
    val visibility: String = "",
    @SerialName("visibleUserIds")
    val visibleUserIds: List<String> = listOf()
) {
    fun toTimelineUiState(): me.sanao1006.core.model.notes.TimelineUiState {
        return me.sanao1006.core.model.notes.TimelineUiState(
            user = user,
            text = text.orEmpty()
        )
    }
}