package me.sanao1006.core.model.requestbody.channel

import kotlinx.serialization.Serializable

@Serializable
data class ChannelListRequestBody(
    val query: String,
    val limit: Int
)
