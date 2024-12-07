package me.sanao1006.core.model

data class LoginUserInfo(
    val userName: String = "",
    val name: String = "",
    val avatarUrl: String = "",
    val followingCount: Int = 0,
    val followersCount: Int = 0,
    val userId: String = "",
    val host: String = ""
)
