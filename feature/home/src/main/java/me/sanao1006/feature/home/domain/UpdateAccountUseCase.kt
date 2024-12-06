package me.sanao1006.feature.home.domain

import kotlinx.serialization.json.Json
import me.sanao1006.core.data.repository.AccountRepository
import me.sanao1006.core.model.LoginUserInfo
import me.sanao1006.datastore.DataStoreRepository
import javax.inject.Inject


class UpdateAccountUseCase @Inject constructor(
    private val accountRepository: AccountRepository,
    private val dataStoreRepository: DataStoreRepository
) {
    suspend operator fun invoke() {
        val user = accountRepository.i(
            body = Json.decodeFromString("{}")
        )
        dataStoreRepository.saveLoginUserInfo(
            loginUserInfo = LoginUserInfo(
                userName = user.username,
                name = user.name ?: "",
                avatarUrl = user.avatarUrl ?: ""
            )
        )
    }
}
