package me.sanao1006.core.model

import kotlinx.serialization.Serializable

@Serializable
data class AuthSessionGenerateRequestBody(val appSecret: String)
