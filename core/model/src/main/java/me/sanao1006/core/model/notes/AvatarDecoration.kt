package me.sanao1006.core.model.notes

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AvatarDecoration(
    @SerialName("angle")
    val angle: Double = 0.0,
    @SerialName("flipH")
    val flipH: Boolean = false,
    @SerialName("id")
    val id: String = "",
    @SerialName("offsetX")
    val offsetX: Double = 0.0,
    @SerialName("offsetY")
    val offsetY: Double = 0.0,
    @SerialName("url")
    val url: String = ""
)
