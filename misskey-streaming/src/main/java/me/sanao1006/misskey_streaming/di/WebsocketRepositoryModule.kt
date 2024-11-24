package me.sanao1006.misskey_streaming.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import me.sanao1006.misskey_streaming.WebsocketRepository
import me.sanao1006.misskey_streaming.WebsocketRepositoryImpl

@Module
@InstallIn(SingletonComponent::class)
abstract class WebsocketRepositoryModule {
    @Binds
    abstract fun bindWebsocketRepository(
        websocketRepositoryImpl: WebsocketRepositoryImpl
    ): WebsocketRepository
}
