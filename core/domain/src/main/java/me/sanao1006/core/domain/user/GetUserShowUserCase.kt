package me.sanao1006.core.domain.user

import me.sanao1006.core.data.repository.UsersRepository
import me.sanao1006.core.model.user.UserScreenUiState
import me.sanao1006.core.model.user.toUserScreenUiState
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetUserShowUserCase @Inject constructor(
    private val usersRepository: UsersRepository
) {
    suspend operator fun invoke(): UserScreenUiState {
        return usersRepository
            .getUsersShow()
            .toUserScreenUiState()
    }
}
