package me.sanao1006.misskey_streaming.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StreamingResponse(
    @SerialName("body")
    val body: BodyX = BodyX(),
    @SerialName("type")
    val type: String = ""
)