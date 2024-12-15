package me.sanao1006.core.model.response.auth

import kotlinx.serialization.Serializable
import me.sanao1006.core.model.common.User

@Serializable
data class AuthSessionUserKeyResponse(
    val accessToken: String,
    val user: User
)
