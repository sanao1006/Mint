package me.sanao1006.core.domain.user

import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import me.sanao1006.core.data.repository.AccountRepository
import me.sanao1006.core.data.repository.UsersRepository
import me.sanao1006.core.model.requestbody.users.UsersShowRequestBody
import me.sanao1006.core.model.uistate.UserScreenUiState
import me.sanao1006.core.model.uistate.toUserScreenUiState
import me.sanao1006.core.network.di.IODispatcher
import timber.log.Timber

@Singleton
class GetUserShowUserCase @Inject constructor(
    private val usersRepository: UsersRepository,
    private val accountRepository: AccountRepository,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(
        isFromDrawer: Boolean,
        usersShowRequestBody: UsersShowRequestBody
    ): UserScreenUiState = withContext(ioDispatcher) {
        try {
            if (isFromDrawer) {
                val response = accountRepository.i()
                response.toUserScreenUiState()
            } else {
                val user = usersRepository.getUsersShow(usersShowRequestBody)
                user.toUserScreenUiState()
            }
        } catch (e: Exception) {
            Timber.e(e)
            UserScreenUiState.Failed
        }
    }
}
