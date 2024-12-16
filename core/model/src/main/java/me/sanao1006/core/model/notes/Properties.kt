package me.sanao1006.core.model.notes

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Properties(
    @SerialName("avgColor")
    val avgColor: String? = null,
    @SerialName("height")
    val height: Int? = null,
    @SerialName("orientation")
    val orientation: Int? = null,
    @SerialName("width")
    val width: Int? = null
)
