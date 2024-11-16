package me.sanao1006.datastore

interface DataStoreRepository {
    suspend fun saveAccessToken(accessToken: String)
    suspend fun getAccessToken(): String?
    suspend fun saveBaseUrl(baseUrl: String)
    suspend fun getBaseUrl(): String?
}