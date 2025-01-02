package me.sanao1006.core.domain.home

import javax.inject.Inject
import me.sanao1006.core.model.LoginUserInfo
import me.sanao1006.datastore.DataStoreRepository

class LogOutUseCase @Inject constructor(
    private val dataStoreRepository: DataStoreRepository
) {
    suspend operator fun invoke() {
        dataStoreRepository.saveAccessToken("")
        dataStoreRepository.saveBaseUrl("")
        dataStoreRepository.saveLoginUserInfo(LoginUserInfo("", "", ""))
    }
}
