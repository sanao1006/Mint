package me.sanao1006.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

@Singleton
class DataStoreRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : DataStoreRepository {
    override suspend fun saveAccessToken(accessToken: String) {
        dataStore.edit { preferences ->
            preferences[PreferenceKeys.ACCESS_TOKEN] = accessToken
        }
    }

    override suspend fun getAccessToken(): String? {
        return dataStore.data.first()[PreferenceKeys.ACCESS_TOKEN]
    }

    override fun flowAccessToken(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[PreferenceKeys.ACCESS_TOKEN] ?: ""
        }
    }

    override fun flowBaseUrl(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[PreferenceKeys.BASE_URL] ?: ""
        }
    }

    override suspend fun saveBaseUrl(baseUrl: String) {
        dataStore.edit { preferences ->
            preferences[PreferenceKeys.BASE_URL] = baseUrl
        }
    }

    override suspend fun getBaseUrl(): String? {
        return dataStore.data.first()[PreferenceKeys.BASE_URL]
    }
}
