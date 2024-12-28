package me.sanao1006.core.domain.home

import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import me.sanao1006.core.data.repository.AccountRepository
import me.sanao1006.core.model.LoginUserInfo
import me.sanao1006.core.network.di.IODispatcher

class UpdateAccountUseCase @Inject constructor(
    private val accountRepository: AccountRepository,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher

) {
    suspend operator fun invoke(): LoginUserInfo {
        return withContext(ioDispatcher) {
            val user = accountRepository.i()
            LoginUserInfo(
                userName = user.username,
                name = user.name ?: "",
                avatarUrl = user.avatarUrl ?: "",
                followersCount = user.followersCount ?: 0,
                followingCount = user.followingCount ?: 0,
                userId = user.id,
                host = user.host ?: ""
            )
        }
    }
}
