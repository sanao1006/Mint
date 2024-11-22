package me.sanao1006.misskey_streaming

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import de.jensklingenberg.ktorfit.Ktorfit
import me.sanao1006.core.model.StreamingApi
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object StreamingModule {
    @Singleton
    @Provides
    fun provideStreamingRepository(
        @StreamingApi
        ktorfit: Ktorfit
    ): StreamingRepository {
        return ktorfit.createStreamingRepository()
    }
}
