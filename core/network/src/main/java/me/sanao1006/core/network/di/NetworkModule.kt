package me.sanao1006.core.network.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import de.jensklingenberg.ktorfit.Ktorfit
import de.jensklingenberg.ktorfit.converter.FlowConverterFactory
import de.jensklingenberg.ktorfit.converter.ResponseConverterFactory
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import javax.inject.Singleton
import kotlin.time.Duration.Companion.seconds
import kotlin.time.DurationUnit
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import me.sanao1006.core.model.NormalApi
import me.sanao1006.datastore.DataStoreRepository
import timber.log.Timber

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    @NormalApi
    fun provideHttpClient(
        json: Json,
        dataStoreRepository: DataStoreRepository
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
                    Timber.tag("MintHttpRequest").d(message)
                }
            }
            level = LogLevel.ALL
        }

        install(Auth) {
            bearer {
                loadTokens {
                    BearerTokens(
                        accessToken = dataStoreRepository.getAccessToken() ?: "",
                        refreshToken = null
                    )
                }
                refreshTokens {
                    BearerTokens(
                        accessToken = dataStoreRepository.getAccessToken() ?: "",
                        refreshToken = null
                    )
                }
            }
        }
        install(DefaultRequest) {
            val baseUrl = runBlocking { dataStoreRepository.getBaseUrl() }
            baseUrl?.let { url(it) }
        }

        defaultRequest {
            contentType(ContentType.Application.Json)
        }
    }

    @Provides
    @Singleton
    fun provideKtorfit(
        @NormalApi
        httpClient: HttpClient
    ): Ktorfit {
        return Ktorfit.Builder()
            .httpClient(httpClient)
            .converterFactories(
                FlowConverterFactory(),
                ResponseConverterFactory()
            )
            .build()
    }
}
