package me.sanao1006.core.data.repository

import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.POST
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import me.sanao1006.core.model.antenna.Antenna
import me.sanao1006.core.model.requestbody.antenna.AntennaIdRequestBody

interface AntennaRepository {
    @POST("api/antennas/list")
    suspend fun getAntennaList(
        @Body body: JsonObject = Json.decodeFromString("{}")
    ): List<Antenna>

    @POST("api/antennas/delete")
    suspend fun deleteAntenna(
        @Body body: AntennaIdRequestBody
    )
}
