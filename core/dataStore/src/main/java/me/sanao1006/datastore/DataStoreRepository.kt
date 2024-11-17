package me.sanao1006.datastore

import kotlinx.coroutines.flow.Flow

interface DataStoreRepository {
    suspend fun saveAccessToken(accessToken: String)
    suspend fun getAccessToken(): String?
    fun flowAccessToken(): Flow<String>
    suspend fun saveBaseUrl(baseUrl: String)
    suspend fun getBaseUrl(): String?
}