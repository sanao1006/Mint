package me.sanao1006.core.network.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Qualifier
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class IODispatcher

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class MainDispatcher

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DefaultDispatcher

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class UnconfinedDispatcher

@InstallIn(SingletonComponent::class)
@Module
object DispatcherModule {
  @DefaultDispatcher
  @Provides
  fun provideDefaultDispatcher(): CoroutineDispatcher {
    return Dispatchers.Default
  }

  @IODispatcher
  @Provides
  fun provideIODispatcher(): CoroutineDispatcher {
    return Dispatchers.IO
  }

  @MainDispatcher
  @Provides
  fun provideMainDispatcher(): CoroutineDispatcher {
    Dispatchers.Unconfined
    return Dispatchers.Main
  }

  @UnconfinedDispatcher
  @Provides
  fun provideUnconfinedDispatcher(): CoroutineDispatcher {
    return Dispatchers.Unconfined
  }
}
