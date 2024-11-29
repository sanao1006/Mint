package me.sanao1006.core.model.home.notes

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
    SPECIFIED("specified"),
}
