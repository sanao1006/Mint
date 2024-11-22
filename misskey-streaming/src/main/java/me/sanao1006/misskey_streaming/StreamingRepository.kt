package me.sanao1006.misskey_streaming

import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.POST
import de.jensklingenberg.ktorfit.http.Query
import kotlinx.coroutines.flow.Flow
import me.sanao1006.misskey_streaming.model.StreamingRequestBody
import me.sanao1006.misskey_streaming.model.StreamingResponse

interface StreamingRepository {
    @POST("streaming")
    fun connectStreamingChannel(
        @Query("i") i: String,
        @Body requestBody: StreamingRequestBody
    ): Flow<StreamingResponse>

    @POST("streaming")
    suspend fun disconnectStreamingChannel(
        @Query("i") i: String,
        @Body requestBody: StreamingRequestBody
    ): Flow<StreamingResponse>
}