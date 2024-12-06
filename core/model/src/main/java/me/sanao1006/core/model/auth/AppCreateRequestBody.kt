package me.sanao1006.core.model.auth

import kotlinx.serialization.Serializable

@Serializable
data class AppCreateRequestBody(
  val name: String,
  val description: String,
  val permission: List<String>,
  val callbackUrl: String? = null
)
