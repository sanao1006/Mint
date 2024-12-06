package me.sanao1006.core.network.di

import kotlinx.coroutines.runBlocking
import me.sanao1006.datastore.DataStoreRepository
import javax.inject.Inject

class BaseUrlModule @Inject constructor(
    private val dataStoreRepository: DataStoreRepository
) {
    private var baseUrl: String? = null

    init {
        runBlocking {
            baseUrl = dataStoreRepository.getBaseUrl()
        }
    }

    fun getBaseUrl(): String? = baseUrl
}
