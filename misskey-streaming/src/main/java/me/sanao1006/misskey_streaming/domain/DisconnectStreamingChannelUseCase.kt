package me.sanao1006.misskey_streaming.domain

import me.sanao1006.datastore.DataStoreRepository
import me.sanao1006.misskey_streaming.StreamingRepository
import me.sanao1006.misskey_streaming.model.Body
import me.sanao1006.misskey_streaming.model.StreamingRequestBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DisconnectStreamingChannelUseCase @Inject constructor(
    private val streamingRepository: StreamingRepository,
    private val dataStoreRepository: DataStoreRepository
) {
    suspend operator fun invoke(id: String) {
        val i = dataStoreRepository.getAccessToken() ?: ""
        streamingRepository.disconnectStreamingChannel(
            i = i,
            requestBody = StreamingRequestBody(
                type = "disconnect",
                body = Body(
                    channel = "disconnect",
                    id = id
                )
            )
        )
    }
}
