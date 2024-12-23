package me.sanao1006.core.model.response.notes

import kotlinx.serialization.Serializable

@Serializable
data class NotesStateResponse(
    val isFavorited: Boolean,
    val isMutedThread: Boolean
)
