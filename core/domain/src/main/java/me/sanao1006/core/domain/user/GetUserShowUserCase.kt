package me.sanao1006.core.domain.user

import javax.inject.Inject
import javax.inject.Singleton
import me.sanao1006.core.data.repository.AccountRepository
import me.sanao1006.core.data.repository.UsersRepository
import me.sanao1006.core.model.requestbody.users.UsersShowRequestBody
import me.sanao1006.core.model.uistate.UserScreenUiState
import me.sanao1006.core.model.uistate.toUserScreenUiState

@Singleton
class GetUserShowUserCase @Inject constructor(
    private val usersRepository: UsersRepository,
    private val accountRepository: AccountRepository
) {
    suspend operator fun invoke(
        isFromDrawer: Boolean,
        usersShowRequestBody: UsersShowRequestBody
    ): UserScreenUiState = try {
        val response = accountRepository.i()
        if (isFromDrawer) {
            response.toUserScreenUiState()
        } else {
            val user = usersRepository.getUsersShow(usersShowRequestBody)
            user.toUserScreenUiState()
        }
    } catch (e: Exception) {
        UserScreenUiState.Failed
    }
}
