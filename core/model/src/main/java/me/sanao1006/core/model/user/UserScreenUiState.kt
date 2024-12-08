package me.sanao1006.core.model.user

import me.sanao1006.core.model.notes.Field
import me.sanao1006.core.model.notes.User

data class UserScreenUiState(
    val username: String = "",
    val name: String? = null,
    val avatarUrl: String? = null,
    val bannerUrl: String? = null,
    val host: String? = null,
    val followingCount: Int = 0,
    val followersCount: Int = 0,
    val description: String? = null,
    val fields: List<Field>? = null,
    val notesCount: Int = 0
)

fun User.toUserScreenUiState() = UserScreenUiState(
    username = username,
    name = name,
    avatarUrl = avatarUrl,
    bannerUrl = bannerUrl,
    host = host,
    followingCount = followingCount,
    followersCount = followersCount,
    description = description,
    fields = fields,
    notesCount = notesCount
)
