package me.sanao1006.core.model.requestbody.antenna

import kotlinx.serialization.Serializable

@Serializable
data class AntennaCreateRequestBody(
    val name: String,
    val src: String,
    val userListId: String? = null,
    val keywords: List<List<String>> = emptyList(),
    val excludeKeywords: List<List<String>> = emptyList(),
    val users: List<String> = emptyList(),
    val canSensitive: Boolean,
    val localOnly: Boolean? = null,
    val excludeBots: Boolean? = null,
    val withReplies: Boolean,
    val withFile: Boolean,
)
