package me.sanao1006.core.model.user

import kotlinx.serialization.Serializable

@Serializable
data class UsersShowRequestBody(
    val userId: String,
    val userIds: List<String>? = null,
    val username: String? = null,
    val host: String? = null
)
