package me.sanao1006.core.data.repository

import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.POST
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import me.sanao1006.core.model.antenna.Antenna
import me.sanao1006.core.model.meta.Note
import me.sanao1006.core.model.requestbody.antenna.AntennaCreateRequestBody
import me.sanao1006.core.model.requestbody.antenna.AntennaIdRequestBody
import me.sanao1006.core.model.requestbody.antenna.AntennaUpdateRequestBody
import me.sanao1006.core.model.requestbody.antenna.AntennasNotesRequestBody

interface AntennaRepository {
    @POST("api/antennas/list")
    suspend fun getAntennaList(
        @Body body: JsonObject = Json.decodeFromString("{}")
    ): List<Antenna>

    @POST("api/antennas/delete")
    suspend fun deleteAntenna(
        @Body body: AntennaIdRequestBody
    )

    @POST("api/antennas/notes")
    suspend fun getAntennaNotes(
        @Body body: AntennasNotesRequestBody
    ): List<Note>

    @POST("api/antennas/create")
    suspend fun createAntenna(
        @Body body: AntennaCreateRequestBody
    ): Antenna

    @POST("api/antennas/update")
    suspend fun updateAntenna(
        @Body body: AntennaUpdateRequestBody
    ): Antenna
}
