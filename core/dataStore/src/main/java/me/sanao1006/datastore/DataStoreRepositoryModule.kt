package me.sanao1006.datastore

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataStoreRepositoryModule {

  @Binds
  abstract fun bindDataStoreRepository(impl: DataStoreRepositoryImpl): DataStoreRepository
}
