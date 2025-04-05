package me.sanao1006.core.model.notes

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BadgeRole(
    @SerialName("displayOrder")
    val displayOrder: Int = 0,
    @SerialName("iconUrl")
    val iconUrl: String? = null,
    @SerialName("name")
    val name: String = ""
)
