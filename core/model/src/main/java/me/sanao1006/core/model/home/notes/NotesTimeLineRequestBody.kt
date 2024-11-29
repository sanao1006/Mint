package me.sanao1006.core.model.home.notes

import kotlinx.serialization.Serializable

@Serializable
data class NotesTimeLineRequestBody(
    val limit: Int = 10,
    val sinceId: String = "",
    val untilId: String = "",
    val sinceDate: Int = 0,
    val untilDate: Int = 0,
    val allowPartial: Boolean = false,
    val includeMyRenotes: Boolean = true,
    val includeRenotedMyNotes: Boolean = true,
    val includeLocalRenotes: Boolean = true,
    val withFiles: Boolean = false,
    val withRenotes: Boolean = true
)
