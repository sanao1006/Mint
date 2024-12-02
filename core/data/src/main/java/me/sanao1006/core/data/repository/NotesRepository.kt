package me.sanao1006.core.data.repository

import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.POST
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.json.JsonObject
import me.sanao1006.core.model.notes.NotesCreateRequestBody
import me.sanao1006.core.model.notes.NotesTimeLineRequestBody
import me.sanao1006.core.model.notes.NotesTimeline

interface NotesRepository {
    @POST("api/notes/create")
    suspend fun createNotes(
        @Body notesCreateRequestBody: NotesCreateRequestBody
    ): JsonObject

    @POST("api/notes/timeline")
    fun flowNotesTimeline(
        @Body notesTimeLineRequestBody: NotesTimeLineRequestBody
    ): Flow<List<NotesTimeline>>

    @POST("api/notes/local-timeline")
    suspend fun getNotesLocalTimeline(
        @Body notesTimeLineRequestBody: NotesTimeLineRequestBody
    ): List<NotesTimeline>

    @POST("api/notes/hybrid-timeline")
    suspend fun getNotesHybridTimeline(
        @Body notesTimeLineRequestBody: NotesTimeLineRequestBody
    ): List<NotesTimeline>

    @POST("api/notes/global-timeline")
    suspend fun getNotesGlobalTimeline(
        @Body notesTimeLineRequestBody: NotesTimeLineRequestBody
    ): List<NotesTimeline>
}