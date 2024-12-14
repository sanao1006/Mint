package me.sanao1006.core.model.notes

data class TimelineItem(
    val user: User? = null,
    val text: String,
    val id: String,
    val visibility: Visibility,
    val uri: String,
    val createdAt: String,
)
