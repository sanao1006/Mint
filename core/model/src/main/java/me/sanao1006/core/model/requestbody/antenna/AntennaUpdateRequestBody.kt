package me.sanao1006.core.model.requestbody.antenna

import kotlinx.serialization.Serializable

@Serializable
data class AntennaUpdateRequestBody(
    val antennaId: String,
    val name: String,
    val src: String,
    val userListId: String? = null,
    val keywords: List<List<String>>? = null,
    val excludeKeywords: List<List<String>>? = null,
    val users: List<String>,
    val caseSensitive: Boolean,
    val localOnly: Boolean,
    val excludeBots: Boolean,
    val withReplies: Boolean,
    val withFile: Boolean
)
