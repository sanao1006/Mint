package me.sanao1006.misskey_streaming

import android.net.Uri
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.client.request.url
import io.ktor.websocket.Frame
import io.ktor.websocket.WebSocketSession
import io.ktor.websocket.close
import io.ktor.websocket.readText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.serialization.json.Json
import me.sanao1006.core.model.StreamingApi
import me.sanao1006.datastore.DataStoreRepository
import me.sanao1006.misskey_streaming.model.Body
import me.sanao1006.misskey_streaming.model.StreamingRequestBody
import me.sanao1006.misskey_streaming.model.StreamingResponse
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Singleton
class WebsocketRepositoryImpl @Inject constructor(
    @StreamingApi
    private val httpClient: HttpClient,
    private val dataStoreRepository: DataStoreRepository,
    private val json: Json
) : WebsocketRepository {
    private var session: StateFlow<WebSocketSession?>? = dataStoreRepository.flowAccessToken()
        .combine(dataStoreRepository.flowBaseUrl()) { i, url ->
            "wss://${Uri.parse(url).schemeSpecificPart}/streaming?i=$i"
        }.map { wsUrl ->
            httpClient.webSocketSession {
                url(wsUrl)
            }
        }.stateIn(
            scope = CoroutineScope(Dispatchers.Default),
            initialValue = null,
            started = SharingStarted.WhileSubscribed(5000),
        )

    @OptIn(ExperimentalUuidApi::class)
    private val id = Uuid.random().toString()

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getSessionStream(): Flow<StreamingResponse> {
        return session?.flatMapLatest { session ->
            session?.incoming?.consumeAsFlow()?.filterIsInstance<Frame.Text>()
                ?.mapNotNull { frame ->
                    json.decodeFromString<StreamingResponse>(frame.readText())
                } ?: emptyFlow()
        } ?: emptyFlow()
    }

    override suspend fun sendAction(streamingChannel: StreamingChannel) {
        val requestBody = StreamingRequestBody(
            type = "connect",
            body = Body(
                channel = streamingChannel.value,
                params = null,
                id = id,
            )
        )
        session?.collectLatest {
            it?.send(
                Frame.Text(
                    json.encodeToString(
                        StreamingRequestBody.serializer(),
                        requestBody
                    )
                )
            )
        }
    }

    override suspend fun close() {
        session?.collectLatest {
            it?.close()
        }
        session = null
    }
}

enum class StreamingChannel(val value: String) {
    HOME("homeTimeline"),
    LOCAL("localTimeline"),
    SOCIAL("hybridTimeline"),
    GLOBAL("globalTimeline"),
    MAIN("main")
}
