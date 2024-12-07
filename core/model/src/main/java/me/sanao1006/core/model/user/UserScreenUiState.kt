package me.sanao1006.core.model.user

import me.sanao1006.core.model.notes.User

data class UserScreenUiState(
    val username: String,
    val name: String? = null,
    val avatarUrl: String? = null
)

fun User.toUserScreenUiState() = UserScreenUiState(
    username = username,
    name = name,
    avatarUrl = avatarUrl
)