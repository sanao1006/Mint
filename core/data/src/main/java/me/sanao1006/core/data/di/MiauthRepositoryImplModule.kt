package me.sanao1006.core.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import me.sanao1006.core.data.repository.MiauthRepositoryImpl
import me.sanao1006.core.network.api.MiauthRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class MiauthRepositoryImplModule {
    @Singleton
    @Binds
    internal abstract fun bindMiauthRepositoryImpl(miauthRepositoryImpl: MiauthRepositoryImpl): MiauthRepository
}
