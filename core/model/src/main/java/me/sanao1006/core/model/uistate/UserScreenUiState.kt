package me.sanao1006.core.model.uistate

import me.sanao1006.core.model.common.Field
import me.sanao1006.core.model.common.User

sealed interface UserScreenUiState {
    data object Loading : UserScreenUiState
    data object Failed : UserScreenUiState

    data class Success(
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
    ) : UserScreenUiState
}

fun User.UserScreen.toUserScreenUiState() = UserScreenUiState.Success(
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
