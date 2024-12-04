package me.sanao1006.core.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Error(
  @SerialName("code")
  val code: String = "",
  @SerialName("id")
  val id: String = "",
  @SerialName("message")
  val message: String = ""
)
