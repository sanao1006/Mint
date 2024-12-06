package me.sanao1006.core.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiError(
  @SerialName("error")
  val error: Error = Error()
)
