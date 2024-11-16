package me.sanao1006.datastore

import androidx.datastore.preferences.core.stringPreferencesKey

object PreferenceKeys {
    val ACCESS_TOKEN = stringPreferencesKey("access_token")
    val BASE_URL = stringPreferencesKey("base_url")
}