package me.sanao1006.core.model.requestbody.antenna

import kotlinx.serialization.Serializable

@Serializable
data class AntennaUpdateRequestBody(
    val antennaId: String,
    val name: String? = null,
    val src: String? = null,
    val userListId: String? = null,
    val keywords: List<List<String>>? = null,
    val excludeKeywords: List<List<String>>? = null,
    val users: List<String>? = null,
    val canSensitive: Boolean? = null,
    val localOnly: Boolean? = null,
    val excludeBots: Boolean? = null,
    val withReplies: Boolean? = null,
    val withFile: Boolean? = null
)
