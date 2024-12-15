package me.sanao1006.core.data.repository

import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.POST
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import me.sanao1006.core.model.notes.User
import me.sanao1006.core.model.requestbody.account.INotificationsRequestBody
import me.sanao1006.core.model.response.notes.NotesTimelineResponse

interface AccountRepository {
    @POST("api/i")
    suspend fun i(
        @Body body: JsonObject = Json.decodeFromString("{}")
    ): User

    @POST("api/i/notifications")
    suspend fun notifications(
        @Body body: INotificationsRequestBody
    ): List<NotesTimelineResponse>
}
