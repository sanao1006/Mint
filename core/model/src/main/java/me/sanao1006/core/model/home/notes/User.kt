package me.sanao1006.core.model.home.notes


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class User(
    @SerialName("avatarBlurhash")
    val avatarBlurhash: String? = null,
    @SerialName("avatarDecorations")
    val avatarDecorations: List<AvatarDecoration> = listOf(),
    @SerialName("avatarUrl")
    val avatarUrl: String = "",
    @SerialName("badgeRoles")
    val badgeRoles: List<BadgeRole> = listOf(),
    @SerialName("emojis")
    val emojis: Emojis = Emojis(),
    @SerialName("host")
    val host: String = "",
    @SerialName("id")
    val id: String = "",
    @SerialName("instance")
    val instance: Instance = Instance(),
    @SerialName("isBot")
    val isBot: Boolean = false,
    @SerialName("isCat")
    val isCat: Boolean = false,
    @SerialName("name")
    val name: String? = null,
    @SerialName("onlineStatus")
    val onlineStatus: String = "",
    @SerialName("username")
    val username: String = ""
)