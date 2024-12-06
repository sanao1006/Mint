package me.sanao1006.core.network.di

import javax.inject.Inject
import kotlinx.coroutines.runBlocking
import me.sanao1006.datastore.DataStoreRepository

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
