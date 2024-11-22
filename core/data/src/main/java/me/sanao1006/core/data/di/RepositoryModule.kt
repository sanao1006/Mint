package me.sanao1006.core.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import de.jensklingenberg.ktorfit.Ktorfit
import me.sanao1006.core.data.repository.MiauthRepository
import me.sanao1006.core.data.repository.NotesRepository
import me.sanao1006.core.data.repository.createMiauthRepository
import me.sanao1006.core.data.repository.createNotesRepository
import me.sanao1006.core.model.NormalApi
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideMiauthRepository(
        @NormalApi
        ktorfit: Ktorfit
    ): MiauthRepository {
        return ktorfit.createMiauthRepository()
    }

    @Provides
    @Singleton
    fun provideNotesRepository(
        @NormalApi
        ktorfit: Ktorfit
    ): NotesRepository {
        return ktorfit.createNotesRepository()
    }
}