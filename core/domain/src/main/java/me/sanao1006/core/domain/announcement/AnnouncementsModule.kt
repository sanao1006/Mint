package me.sanao1006.core.domain.announcement

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AnnouncementsModule {
    @Binds
    abstract fun bindGetAnnouncementsUseCase(
        getAnnouncementsUseCaseImpl: GetAnnouncementsUseCaseImpl
    ): GetAnnouncementsUseCase
}
