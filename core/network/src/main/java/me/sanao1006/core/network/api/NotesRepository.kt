package me.sanao1006.core.network.api

import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.POST
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.json.JsonObject
import me.sanao1006.core.model.home.notes.NotesTimeLineRequestBody

interface NotesRepository {

    @POST("api/notes/timeline")
    fun getNotesTimeline(
        @Body notesTimeLineRequestBody: NotesTimeLineRequestBody
    ): Flow<List<JsonObject>>
}