package me.sanao1006.core.model.auth

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
data class AuthSessionUserKeyResponse(
    val accessToken: String,
    val user: JsonObject
)
