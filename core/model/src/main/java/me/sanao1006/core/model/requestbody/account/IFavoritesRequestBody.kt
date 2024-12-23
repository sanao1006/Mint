package me.sanao1006.core.model.requestbody.account

import kotlinx.serialization.Serializable

@Serializable
data class IFavoritesRequestBody(
    val limit: Int = 10,
    val sinceId: String? = null,
    val untilId: String? = null
)
