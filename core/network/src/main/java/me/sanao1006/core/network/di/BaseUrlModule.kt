package me.sanao1006.core.network.di

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.sanao1006.datastore.DataStoreRepository
import javax.inject.Inject

class BaseUrlModule @Inject constructor(
    private val dataStoreRepository: DataStoreRepository
) {
    private var baseUrl: String? = null

    init {
        CoroutineScope(Dispatchers.IO).launch {
            if (dataStoreRepository.getBaseUrl().isNullOrBlank()) {
                baseUrl = dataStoreRepository.getBaseUrl()
            }
        }
    }

    fun getBaseUrl(): String? = baseUrl
}