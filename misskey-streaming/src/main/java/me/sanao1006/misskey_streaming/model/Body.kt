package me.sanao1006.misskey_streaming.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Body(
    @SerialName("channel")
    val channel: String = "",
    @SerialName("id")
    val id: String = "",
    @SerialName("params")
    val params: Params? = null
)