package me.sanao1006.core.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import de.jensklingenberg.ktorfit.Ktorfit
import javax.inject.Singleton
import me.sanao1006.core.data.repository.AccountRepository
import me.sanao1006.core.data.repository.MiauthRepository
import me.sanao1006.core.data.repository.NotesRepository
import me.sanao1006.core.data.repository.UsersRepository
import me.sanao1006.core.data.repository.createAccountRepository
import me.sanao1006.core.data.repository.createMiauthRepository
import me.sanao1006.core.data.repository.createNotesRepository
import me.sanao1006.core.data.repository.createUsersRepository

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

    @Provides
    @Singleton
    fun provideAccountRepository(
        ktorfit: Ktorfit
    ): AccountRepository {
        return ktorfit.createAccountRepository()
    }

    @Provides
    @Singleton
    fun provideUsersRepository(
        ktorfit: Ktorfit
    ): UsersRepository {
        return ktorfit.createUsersRepository()
    }
}
