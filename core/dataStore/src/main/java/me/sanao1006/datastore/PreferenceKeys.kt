package me.sanao1006.datastore

import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object PreferenceKeys {
    val ACCESS_TOKEN = stringPreferencesKey("access_token")
    val BASE_URL = stringPreferencesKey("base_url")
    val LOGIN_USER_NAME = stringPreferencesKey("login_user_name")
    val LOGIN_NAME = stringPreferencesKey("login_name")
    val LOGIN_AVATAR_URL = stringPreferencesKey("login_avatar_url")
    val LOGIN_FOLLOWERS_COUNT = intPreferencesKey("login_followers_count")
    val LOGIN_FOLLOWING_COUNT = intPreferencesKey("login_following_count")
    val LOGIN_USER_ID = stringPreferencesKey("login_user_id")
    val LOGIN_HOST = stringPreferencesKey("login_host")
}
