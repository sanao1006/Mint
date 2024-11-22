package me.sanao1006.misskey_streaming.domain

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import me.sanao1006.datastore.DataStoreRepository
import me.sanao1006.misskey_streaming.StreamingRepository
import me.sanao1006.misskey_streaming.model.Body
import me.sanao1006.misskey_streaming.model.StreamingRequestBody
import me.sanao1006.misskey_streaming.model.StreamingResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FlowStreamingSocialTimelineUseCase @Inject constructor(
    private val streamingRepository: StreamingRepository,
    private val dataStoreRepository: DataStoreRepository
) {
    val iFlow = dataStoreRepository.flowAccessToken()

    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(id: String): Flow<StreamingResponse> {
        return iFlow.flatMapLatest { i ->
            streamingRepository.connectStreamingChannel(
                i = i,
                requestBody = StreamingRequestBody(
                    type = "channel",
                    body = Body(
                        channel = "hybridTimeline",
                        id = id,
                    )
                )
            )
        }
    }
}
