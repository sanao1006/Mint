package me.sanao1006.core.model.requestbody.auth

import kotlinx.serialization.Serializable

@Serializable
data class AuthSessionGenerateRequestBody(val appSecret: String)
