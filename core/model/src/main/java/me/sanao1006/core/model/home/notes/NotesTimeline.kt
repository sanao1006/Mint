package me.sanao1006.core.model.home.notes


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NotesTimeline(
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
    val myReaction: MyReaction? = MyReaction(),
    @SerialName("poll")
    val poll: Poll? = Poll(),
    @SerialName("reactionAcceptance")
    val reactionAcceptance: String? = null,
    @SerialName("reactionAndUserPairCache")
    val reactionAndUserPairCache: List<String> = listOf(),
    @SerialName("reactions")
    val reactions: Reactions? = Reactions(),
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
    val user: User? = User(),
    @SerialName("userId")
    val userId: String = "",
    @SerialName("visibility")
    val visibility: String = "",
    @SerialName("visibleUserIds")
    val visibleUserIds: List<String> = listOf()
) {
    fun toTimelineUiState(): TimelineUiState {
        return TimelineUiState(
            user = user,
            text = text.orEmpty()
        )
    }
}