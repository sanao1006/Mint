package me.sanao1006.core.model.notes

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Instance(
    @SerialName("faviconUrl")
    val faviconUrl: String? = null,
    @SerialName("iconUrl")
    val iconUrl: String? = null,
    @SerialName("name")
    val name: String? = null,
    @SerialName("softwareName")
    val softwareName: String? = null,
    @SerialName("softwareVersion")
    val softwareVersion: String? = null,
    @SerialName("themeColor")
    val themeColor: String? = null
)
