package me.sanao1006.core.domain.user

import javax.inject.Inject
import javax.inject.Singleton
import me.sanao1006.core.data.repository.AccountRepository
import me.sanao1006.core.data.repository.UsersRepository
import me.sanao1006.core.model.user.UserScreenUiState
import me.sanao1006.core.model.user.UsersShowRequestBody
import me.sanao1006.core.model.user.toUserScreenUiState

@Singleton
class GetUserShowUserCase @Inject constructor(
    private val usersRepository: UsersRepository,
    private val accountRepository: AccountRepository
) {
    suspend operator fun invoke(
        isFromDrawer: Boolean,
        usersShowRequestBody: UsersShowRequestBody
    ): UserScreenUiState {
        if (isFromDrawer) {
            val user = accountRepository.i()
            return UserScreenUiState(
                username = user.username,
                name = user.name,
                avatarUrl = user.avatarUrl,
                bannerUrl = user.bannerUrl,
                host = user.host,
                followingCount = user.followingCount,
                followersCount = user.followersCount,
                description = user.description,
                fields = user.fields,
                notesCount = user.notesCount
            )
        }

        return usersRepository
            .getUsersShow(usersShowRequestBody)
            .toUserScreenUiState()
    }
}
