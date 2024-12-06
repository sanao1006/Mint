package me.sanao1006.datastore

import androidx.datastore.preferences.core.stringPreferencesKey

object PreferenceKeys {
    val ACCESS_TOKEN = stringPreferencesKey("access_token")
    val BASE_URL = stringPreferencesKey("base_url")
    val LOGIN_USER_NAME = stringPreferencesKey("login_user_name")
    val LOGIN_NAME = stringPreferencesKey("login_name")
    val LOGIN_AVATAR_URL = stringPreferencesKey("login_avatar_url")
}
