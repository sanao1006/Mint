package me.sanao1006.misskey_streaming.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.pingInterval
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.KotlinxWebsocketSerializationConverter
import kotlinx.serialization.json.Json
import me.sanao1006.core.model.StreamingApi
import timber.log.Timber
import javax.inject.Singleton
import kotlin.time.DurationUnit
import kotlin.time.toDuration

@Module
@InstallIn(SingletonComponent::class)
object WebSocketModule {

    @Singleton
    @Provides
    @StreamingApi
    fun provideHttpClient(
        json: Json
    ): HttpClient = HttpClient(OkHttp) {
        install(WebSockets) {
            contentConverter = KotlinxWebsocketSerializationConverter(json)
            pingInterval = 2.toDuration(DurationUnit.SECONDS)

        }
        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    Timber.tag("MintWebsocket").d(message)
                }
            }
            level = LogLevel.ALL
        }
        defaultRequest {
            contentType(ContentType.Application.Json)
        }
    }
}