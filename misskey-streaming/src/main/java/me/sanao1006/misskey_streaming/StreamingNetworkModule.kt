package me.sanao1006.misskey_streaming

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import de.jensklingenberg.ktorfit.Ktorfit
import de.jensklingenberg.ktorfit.converter.FlowConverterFactory
import de.jensklingenberg.ktorfit.converter.ResponseConverterFactory
import io.ktor.client.HttpClient
import kotlinx.coroutines.runBlocking
import me.sanao1006.core.model.StreamingApi
import me.sanao1006.core.network.di.BaseUrlModule
import me.sanao1006.datastore.DataStoreRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object StreamingNetworkModule {
    @Provides
    @Singleton
    @StreamingApi
    fun provideKtorfit(
        httpClient: HttpClient,
        dataStoreRepository: DataStoreRepository
    ): Ktorfit {
        return Ktorfit.Builder()
            .httpClient(httpClient)
            .let {
                runBlocking {
                    it.baseUrl("wss://${BaseUrlModule(dataStoreRepository).getBaseUrl()}/")
                }
            }
            .converterFactories(
                FlowConverterFactory(),
                ResponseConverterFactory(),
            )
            .build()
    }
}