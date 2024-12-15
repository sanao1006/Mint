package me.sanao1006.core.model.notes

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Choice(
    @SerialName("isVoted")
    val isVoted: Boolean,
    @SerialName("text")
    val text: String,
    @SerialName("votes")
    val votes: Int
)
