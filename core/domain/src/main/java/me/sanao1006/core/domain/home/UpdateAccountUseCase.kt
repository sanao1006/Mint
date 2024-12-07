package me.sanao1006.core.domain.home

import me.sanao1006.core.data.repository.AccountRepository
import me.sanao1006.core.model.LoginUserInfo
import me.sanao1006.datastore.DataStoreRepository
import javax.inject.Inject

class UpdateAccountUseCase @Inject constructor(
    private val accountRepository: AccountRepository,
    private val dataStoreRepository: DataStoreRepository
) {
    suspend operator fun invoke(): LoginUserInfo {
        val user = accountRepository.i()
        return LoginUserInfo(
            userName = user.username,
            name = user.name ?: "",
            avatarUrl = user.avatarUrl ?: "",
            followersCount = user.followersCount,
            followingCount = user.followingCount,
            userId = user.id,
            host = user.host ?: ""
        )
    }
}
