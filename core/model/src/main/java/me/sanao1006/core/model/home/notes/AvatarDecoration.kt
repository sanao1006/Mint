package me.sanao1006.core.model.home.notes


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AvatarDecoration(
    @SerialName("angle")
    val angle: Int = 0,
    @SerialName("flipH")
    val flipH: Boolean = false,
    @SerialName("id")
    val id: String = "",
    @SerialName("offsetX")
    val offsetX: Int = 0,
    @SerialName("offsetY")
    val offsetY: Int = 0,
    @SerialName("url")
    val url: String = ""
)