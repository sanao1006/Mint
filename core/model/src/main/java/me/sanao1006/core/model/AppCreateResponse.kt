package me.sanao1006.core.model

import kotlinx.serialization.Serializable

@Serializable
data class AppCreateResponse(
    val id: String,
    val name: String,
    val description: String,
    val callbackUrl: String?,
    val permission: String,
    val secret: String
)
