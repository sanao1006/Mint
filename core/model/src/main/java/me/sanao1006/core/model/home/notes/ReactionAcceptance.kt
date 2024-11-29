package me.sanao1006.core.model.home.notes

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class ReactionAcceptance(val value: String) {
    @SerialName("likeOnly")
    LIKE_ONLY("likeOnly"),

    @SerialName("likeOnlyForRemote")
    LIKE_ONLY_FOR_REMOTE("likeOnlyForRemote"),

    @SerialName("nonSensitiveOnly")
    NON_SENSITIVE_ONLY("nonSensitiveOnly"),
}
