package me.sanao1006.core.data.repository

import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.POST
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import me.sanao1006.core.model.notes.User

interface AccountRepository {
    @POST("api/i")
    suspend fun i(
        @Body body: JsonObject = Json.decodeFromString("{}")
    ): User
}
