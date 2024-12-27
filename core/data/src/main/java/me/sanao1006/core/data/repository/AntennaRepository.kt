package me.sanao1006.core.data.repository

import de.jensklingenberg.ktorfit.http.POST
import me.sanao1006.core.model.antenna.AntennaResponse

interface AntennaRepository {
    @POST("antennas/list")
    suspend fun getAntennaList(): List<AntennaResponse>
}
