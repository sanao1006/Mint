package me.sanao1006.core.model.notes

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Channel(
    @SerialName("allowRenoteToExternal")
    val allowRenoteToExternal: Boolean = false,
    @SerialName("color")
    val color: String = "",
    @SerialName("id")
    val id: String = "",
    @SerialName("isSensitive")
    val isSensitive: Boolean = false,
    @SerialName("name")
    val name: String = ""
)
