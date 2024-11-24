package me.sanao1006.misskey_streaming

import kotlinx.coroutines.flow.Flow
import me.sanao1006.misskey_streaming.model.StreamingResponse

interface WebsocketRepository {
    fun getSessionStream(): Flow<StreamingResponse>
    suspend fun sendAction(streamingChannel: StreamingChannel)
    suspend fun close()
}