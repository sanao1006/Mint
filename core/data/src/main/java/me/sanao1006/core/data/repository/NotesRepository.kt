package me.sanao1006.core.data.repository

import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.POST
import kotlinx.coroutines.flow.Flow
import me.sanao1006.core.model.home.notes.NotesTimeLineRequestBody
import me.sanao1006.core.model.home.notes.NotesTimeline

interface NotesRepository {

    @POST("api/notes/timeline")
    fun flowNotesTimeline(
        @Body notesTimeLineRequestBody: NotesTimeLineRequestBody
    ): Flow<List<NotesTimeline>>

    @POST("api/notes/hybrid-timeline")
    suspend fun getNotesTimeline(
        @Body notesTimeLineRequestBody: NotesTimeLineRequestBody
    ): List<NotesTimeline>
}