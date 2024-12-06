package me.sanao1006.datastore

import kotlinx.coroutines.flow.Flow
import me.sanao1006.core.model.LoginUserInfo

interface DataStoreRepository {
    suspend fun saveAccessToken(accessToken: String)
    suspend fun getAccessToken(): String?
    fun flowAccessToken(): Flow<String>
    fun flowBaseUrl(): Flow<String>
    suspend fun saveBaseUrl(baseUrl: String)
    suspend fun getBaseUrl(): String?
    suspend fun saveLoginUserInfo(loginUserInfo: LoginUserInfo)
    suspend fun getLoginUserInfo(): LoginUserInfo
}
