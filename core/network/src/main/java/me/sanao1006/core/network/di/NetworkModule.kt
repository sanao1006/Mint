package me.sanao1006.core.network.di

import android.util.Log
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import de.jensklingenberg.ktorfit.Ktorfit
import de.jensklingenberg.ktorfit.converter.FlowConverterFactory
import de.jensklingenberg.ktorfit.converter.ResponseConverterFactory
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import javax.inject.Singleton
import kotlin.time.Duration.Companion.seconds
import kotlin.time.DurationUnit


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideHttpClient(
        json: Json
    ): HttpClient = HttpClient(OkHttp) {
        install(ContentNegotiation) { json(json) }
        install(HttpTimeout) {
            requestTimeoutMillis = 60.seconds.toLong(DurationUnit.MILLISECONDS)
            socketTimeoutMillis = 30.seconds.toLong(DurationUnit.MILLISECONDS)
        }
        install(HttpRequestRetry) {
            noRetry()
            exponentialDelay()
        }
        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    Log.d("MintHttpRequest", message)
                }
            }
            level = LogLevel.ALL

        }
    }

    @Provides
    @Singleton
    fun provideKtorfit(): Ktorfit {
        return Ktorfit.Builder()
            .baseUrl("https://swapi.dev/api/")
            .converterFactories(
                FlowConverterFactory(),
                ResponseConverterFactory()
            )
            .build()
    }
}