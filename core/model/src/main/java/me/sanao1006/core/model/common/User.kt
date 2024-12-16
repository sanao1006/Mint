package me.sanao1006.core.model.common

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import me.sanao1006.core.model.notes.AvatarDecoration
import me.sanao1006.core.model.notes.BadgeRole
import me.sanao1006.core.model.notes.Emojis
import me.sanao1006.core.model.notes.Instance

@Serializable
data class User(
    val avatarBlurhash: String? = null,
    val avatarDecorations: List<AvatarDecoration> = emptyList(),
    val avatarUrl: String? = null,
    val badgeRoles: List<BadgeRole>? = null,
    val emojis: Emojis = Emojis(),
    val host: String? = null,
    val id: String = "",
    val instance: Instance = Instance(),
    val isBot: Boolean = false,
    val isCat: Boolean = false,
    val name: String? = null,
    val onlineStatus: String? = null,
    val username: String = "",
    val followingCount: Int? = null,
    val followersCount: Int? = null,
    val bannerUrl: String? = null,
    val description: String? = null,
    val fields: List<Field>? = null,
    val notesCount: Int? = null,
    val requireSigninToViewContents: Boolean? = null
)

@Serializable
data class Field(
    @SerialName("name")
    val name: String = "",
    @SerialName("value")
    val value: String = ""
)
