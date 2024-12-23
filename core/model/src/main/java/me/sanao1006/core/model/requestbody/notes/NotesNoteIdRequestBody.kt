package me.sanao1006.core.model.requestbody.notes

import kotlinx.serialization.Serializable

@Serializable
data class NotesNoteIdRequestBody(
    val noteId: String
)
