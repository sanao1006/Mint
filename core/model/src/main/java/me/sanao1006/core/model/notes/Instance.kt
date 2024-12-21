package me.sanao1006.core.model.notes

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Instance(
    @SerialName("faviconUrl")
    val faviconUrl: String = "",
    @SerialName("iconUrl")
    val iconUrl: String = "",
    @SerialName("name")
    val name: String = "",
    @SerialName("softwareName")
    val softwareName: String = "",
    @SerialName("softwareVersion")
    val softwareVersion: String = "",
    @SerialName("themeColor")
    val themeColor: String = ""
)

fun Instance.isEmpty(): Boolean {
    return this.faviconUrl.isEmpty() &&
            this.iconUrl.isEmpty() &&
            this.name.isEmpty() &&
            this.softwareName.isEmpty() &&
            this.softwareVersion.isEmpty() &&
            this.themeColor.isEmpty()
}