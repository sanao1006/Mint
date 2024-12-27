package me.sanao1006.core.model.requestbody.antenna

import kotlinx.serialization.Serializable

@Serializable
data class AntennaIdRequestBody(
    val antennaId: String
)
