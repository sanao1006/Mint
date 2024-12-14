package me.sanao1006.core.domain.user

import me.sanao1006.core.data.repository.AccountRepository
import me.sanao1006.core.data.repository.UsersRepository
import me.sanao1006.core.model.requestbody.users.UsersShowRequestBody
import me.sanao1006.core.model.uistate.UserScreenUiState
import me.sanao1006.core.model.uistate.toUserScreenUiState
import javax.inject.Inject
import javax.inject.Singleton

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
            return UserScreenUiState.Success(
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
