package me.sanao1006.core.model.notes

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class Visibility(val value: String) {
    @SerialName("public")
    PUBLIC("public"),

    @SerialName("home")
    HOME("home"),

    @SerialName("followers")
    FOLLOWERS("followers"),

    @SerialName("specified")
    SPECIFIED("specified");

    companion object {
        fun get(value: String): Visibility {
            return when (value) {
                "public" -> PUBLIC
                "home" -> HOME
                "followers" -> FOLLOWERS
                "specified" -> SPECIFIED
                else -> PUBLIC
            }
        }
    }
}
