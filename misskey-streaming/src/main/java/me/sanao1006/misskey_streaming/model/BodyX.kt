package me.sanao1006.misskey_streaming.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
data class BodyX(
    @SerialName("body")
    val body: JsonObject? = null,
    @SerialName("id")
    val id: String = "",
    @SerialName("type")
    val type: String = ""
)