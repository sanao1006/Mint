package me.sanao1006.core.model.requestbody.meta

import kotlinx.serialization.Serializable

@Serializable
data class MetaAnnouncementsRequestBody(
    val limit: Int,
    val sinceId: String? = null,
    val untilId: String? = null,
    val isActive: Boolean
)
