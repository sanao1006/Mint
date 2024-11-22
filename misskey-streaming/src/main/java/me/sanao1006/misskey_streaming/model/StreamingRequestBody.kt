package me.sanao1006.misskey_streaming.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StreamingRequestBody(
    @SerialName("body")
    val body: Body = Body(),
    @SerialName("type")
    val type: String = ""
)