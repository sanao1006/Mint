package me.sanao1006.core.model.notes

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Poll(
    @SerialName("choices")
    val choices: List<Choice>,
    @SerialName("expiresAt")
    val expiresAt: String?,
    @SerialName("multiple")
    val multiple: Boolean
)
