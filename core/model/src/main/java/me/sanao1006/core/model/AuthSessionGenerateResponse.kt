package me.sanao1006.core.model

import kotlinx.serialization.Serializable

@Serializable
data class AuthSessionGenerateResponse(
    val token: String,
    val url: String
)
