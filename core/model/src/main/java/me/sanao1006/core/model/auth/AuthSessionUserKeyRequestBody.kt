package me.sanao1006.core.model.auth

import kotlinx.serialization.Serializable

@Serializable
data class AuthSessionUserKeyRequestBody(
  val appSecret: String,
  val token: String
)
