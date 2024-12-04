package me.sanao1006.core.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import de.jensklingenberg.ktorfit.Ktorfit
import javax.inject.Singleton
import me.sanao1006.core.data.repository.MiauthRepository
import me.sanao1006.core.data.repository.NotesRepository
import me.sanao1006.core.data.repository.createMiauthRepository
import me.sanao1006.core.data.repository.createNotesRepository

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideMiauthRepository(
        ktorfit: Ktorfit
    ): MiauthRepository {
        return ktorfit.createMiauthRepository()
    }

    @Provides
    @Singleton
    fun provideNotesRepository(
        ktorfit: Ktorfit
    ): NotesRepository {
        return ktorfit.createNotesRepository()
    }
}
