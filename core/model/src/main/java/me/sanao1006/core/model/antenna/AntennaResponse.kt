package me.sanao1006.core.model.antenna

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AntennaResponse(
    @SerialName("caseSensitive")
    val caseSensitive: Boolean,
    @SerialName("createdAt")
    val createdAt: String,
    @SerialName("excludeBots")
    val excludeBots: Boolean,
    @SerialName("excludeKeywords")
    val excludeKeywords: List<List<String>>,
    @SerialName("hasUnreadNote")
    val hasUnreadNote: Boolean,
    @SerialName("id")
    val id: String,
    @SerialName("isActive")
    val isActive: Boolean,
    @SerialName("keywords")
    val keywords: List<List<String>>,
    @SerialName("localOnly")
    val localOnly: Boolean,
    @SerialName("name")
    val name: String,
    @SerialName("notify")
    val notify: Boolean,
    @SerialName("src")
    val src: String,
    @SerialName("userListId")
    val userListId: List<String>? = null,
    @SerialName("users")
    val users: List<String>,
    @SerialName("withFile")
    val withFile: Boolean,
    @SerialName("withReplies")
    val withReplies: Boolean
)
