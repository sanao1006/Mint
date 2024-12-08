package me.sanao1006.core.model.notes

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class User(
    @SerialName("avatarBlurhash")
    val avatarBlurhash: String? = null,
    @SerialName("avatarDecorations")
    val avatarDecorations: List<me.sanao1006.core.model.notes.AvatarDecoration> = listOf(),
    @SerialName("avatarUrl")
    val avatarUrl: String? = null,
    @SerialName("badgeRoles")
    val badgeRoles: List<me.sanao1006.core.model.notes.BadgeRole> = listOf(),
    @SerialName("emojis")
    val emojis: me.sanao1006.core.model.notes.Emojis = me.sanao1006.core.model.notes.Emojis(),
    @SerialName("host")
    val host: String? = null,
    @SerialName("id")
    val id: String = "",
    @SerialName("instance")
    val instance: me.sanao1006.core.model.notes.Instance = me.sanao1006.core.model.notes.Instance(),
    @SerialName("isBot")
    val isBot: Boolean = false,
    @SerialName("isCat")
    val isCat: Boolean = false,
    @SerialName("name")
    val name: String? = null,
    @SerialName("onlineStatus")
    val onlineStatus: String? = null,
    @SerialName("username")
    val username: String = "",
    @SerialName("followingCount")
    val followingCount: Int = 0,
    @SerialName("followersCount")
    val followersCount: Int = 0,
    @SerialName("bannerUrl")
    val bannerUrl: String? = null,
)
