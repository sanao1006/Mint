package me.sanao1006.core.data.repository

import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.POST
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.json.JsonObject
import me.sanao1006.core.model.meta.Note
import me.sanao1006.core.model.requestbody.notes.NotesCreateRequestBody
import me.sanao1006.core.model.requestbody.notes.NotesNoteIdRequestBody
import me.sanao1006.core.model.requestbody.notes.NotesTimeLineRequestBody
import me.sanao1006.core.model.response.notes.NotesStateResponse

interface NotesRepository {
    @POST("api/notes/create")
    suspend fun createNotes(
        @Body notesCreateRequestBody: NotesCreateRequestBody
    ): JsonObject

    @POST("api/notes/timeline")
    fun flowNotesTimeline(
        @Body notesTimeLineRequestBody: NotesTimeLineRequestBody
    ): Flow<List<Note>>

    @POST("api/notes/timeline")
    suspend fun getNotesHomeTimeline(
        @Body notesTimeLineRequestBody: NotesTimeLineRequestBody
    ): List<Note>

    @POST("api/notes/local-timeline")
    suspend fun getNotesLocalTimeline(
        @Body notesTimeLineRequestBody: NotesTimeLineRequestBody
    ): List<Note>

    @POST("api/notes/hybrid-timeline")
    suspend fun getNotesHybridTimeline(
        @Body notesTimeLineRequestBody: NotesTimeLineRequestBody
    ): List<Note>

    @POST("api/notes/global-timeline")
    suspend fun getNotesGlobalTimeline(
        @Body notesTimeLineRequestBody: NotesTimeLineRequestBody
    ): List<Note>

    @POST("api/notes/favorites/create")
    suspend fun createNotesFavorites(
        @Body body: NotesNoteIdRequestBody
    )

    @POST("api/notes/favorites/delete")
    suspend fun deleteNotesFavorites(
        @Body body: NotesNoteIdRequestBody
    )

    @POST("api/notes/state")
    suspend fun getNotesState(
        @Body body: NotesNoteIdRequestBody
    ): NotesStateResponse
}
