package me.sanao1006.core.model.auth

import kotlinx.serialization.Serializable

@Serializable
data class AppCreateResponse(
  val id: String,
  val name: String,
  val description: String? = null,
  val callbackUrl: String?,
  val permission: List<String>,
  val secret: String
)
