package me.sanao1006.core.model.requestbody.account

import kotlinx.serialization.Serializable

@Serializable
data class INotificationsRequestBody(
    val limit: Int = 10,
    val sinceId: String? = null,
    val untilId: String? = null,
    val markAsRead: Boolean = true,
    val includeTypes: List<IncludeType> = emptyList(),
    val excludeTypes: List<ExcludeType> = emptyList()
)

enum class NotificationType(val value: String) {
    NOTES("note"),
    FOLLOWS("follow"),
    MENTION("mention"),
    REPLY("reply"),
    RENOTE("renote"),
    QUOTE("quote"),
    REACTION("reaction"),
    POLL_ENDED("pollEnded"),
    RECEIVE_FOLLOW_REQUEST("receiveFollowRequest"),
    FOLLOW_REQUEST_ACCEPTED("followRequestAccepted"),
    ROLE_ASSIGNED("roleAssigned"),
    ACHIEVEMENT_EARNED("achievementEarned"),
    EXPORT_COMPLETED("exportCompleted"),
    LOGIN("login"),
    APP("app"),
    TEST("test"),
    POLL_VOTE("pollVote"),
    GROUP_INVITED("groupInvited");

    companion object {
        fun get(value: String): NotificationType =
            entries.first { it.value == value }
    }
}

@Serializable
enum class IncludeType(val value: String) {
    NOTES("note"),
    FOLLOWS("follow"),
    MENTION("mention"),
    REPLY("reply"),
    RENOTE("renote"),
    QUOTE("quote"),
    REACTION("reaction"),
    POLL_ENDED("pollEnded"),
    RECEIVE_FOLLOW_REQUEST("receiveFollowRequest"),
    FOLLOW_REQUEST_ACCEPTED("followRequestAccepted"),
    ROLE_ASSIGNED("roleAssigned"),
    ACHIEVEMENT_EARNED("achievementEarned"),
    EXPORT_COMPLETED("exportCompleted"),
    LOGIN("login"),
    APP("app"),
    TEST("test"),
    POLL_VOTE("pollVote"),
    GROUP_INVITED("groupInvited")
}

@Serializable
enum class ExcludeType(val value: String) {
    NOTES("note"),
    FOLLOWS("follow"),
    MENTION("mention"),
    REPLY("reply"),
    RENOTE("renote"),
    QUOTE("quote"),
    REACTION("reaction"),
    POLL_ENDED("pollEnded"),
    RECEIVE_FOLLOW_REQUEST("receiveFollowRequest"),
    FOLLOW_REQUEST_ACCEPTED("followRequestAccepted"),
    ROLE_ASSIGNED("roleAssigned"),
    ACHIEVEMENT_EARNED("achievementEarned"),
    EXPORT_COMPLETED("exportCompleted"),
    LOGIN("login"),
    APP("app"),
    TEST("test"),
    POLL_VOTE("pollVote"),
    GROUP_INVITED("groupInvited")
}
