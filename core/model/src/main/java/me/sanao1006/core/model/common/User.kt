package me.sanao1006.core.model.common

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.jsonObject
import me.sanao1006.core.model.notes.AvatarDecoration
import me.sanao1006.core.model.notes.BadgeRole
import me.sanao1006.core.model.notes.Emojis
import me.sanao1006.core.model.notes.Instance

@Serializable(with = UserSerializer::class)
sealed class User {
    abstract val avatarBlurhash: String?
    abstract val avatarDecorations: List<AvatarDecoration>
    abstract val avatarUrl: String?
    abstract val badgeRoles: List<BadgeRole>?
    abstract val emojis: Emojis
    abstract val host: String?
    abstract val id: String
    abstract val instance: Instance
    abstract val isBot: Boolean
    abstract val isCat: Boolean
    abstract val name: String?
    abstract val onlineStatus: String?
    abstract val username: String
    abstract val followingCount: Int?
    abstract val followersCount: Int?

    @Serializable
    data class Timeline(
        val bannerUrl: String? = null,
        val description: String? = null,
        val fields: List<Field>? = null,
        override val followingCount: Int = 0,
        override val followersCount: Int = 0,
        override val avatarBlurhash: String? = null,
        override val avatarDecorations: List<AvatarDecoration> = emptyList(),
        override val avatarUrl: String? = null,
        override val badgeRoles: List<BadgeRole>? = null,
        override val emojis: Emojis = Emojis(),
        override val host: String? = null,
        override val id: String = "",
        override val instance: Instance = Instance(),
        override val isBot: Boolean = false,
        override val isCat: Boolean = false,
        override val name: String? = null,
        override val onlineStatus: String? = null,
        override val username: String = ""
    ) : User()

    @Serializable
    data class UserScreen(
        val notesCount: Int = 0,
        val bannerUrl: String? = null,
        val description: String? = null,
        val fields: List<Field>? = null,
        override val followersCount: Int = 0,
        override val followingCount: Int = 0,
        override val avatarBlurhash: String? = null,
        override val avatarDecorations: List<AvatarDecoration> = emptyList(),
        override val avatarUrl: String? = null,
        override val badgeRoles: List<BadgeRole>? = null,
        override val emojis: Emojis = Emojis(),
        override val host: String? = null,
        override val id: String = "",
        override val instance: Instance = Instance(),
        override val isBot: Boolean = false,
        override val isCat: Boolean = false,
        override val name: String? = null,
        override val onlineStatus: String? = null,
        override val username: String = ""
    ) : User()

    @Serializable
    data class Notification(
//        val makeNotesFollowersOnlyBefore: Any?,
//        val makeNotesHiddenBefore: Any?,
        val requireSigninToViewContents: Boolean? = null,
        override val followingCount: Int? = null,
        override val followersCount: Int? = null,
        override val avatarBlurhash: String? = null,
        override val avatarDecorations: List<AvatarDecoration> = emptyList(),
        override val avatarUrl: String? = null,
        override val badgeRoles: List<BadgeRole>? = null,
        override val emojis: Emojis = Emojis(),
        override val host: String? = null,
        override val id: String = "",
        override val instance: Instance = Instance(),
        override val isBot: Boolean = false,
        override val isCat: Boolean = false,
        override val name: String? = null,
        override val onlineStatus: String? = null,
        override val username: String = ""
    ) : User()
}

@Serializable
data class Field(
    @SerialName("name")
    val name: String = "",
    @SerialName("value")
    val value: String = ""
)

// TODO Needs Improvement
@Serializer(forClass = User::class)
object UserSerializer : KSerializer<User> {
    override fun serialize(encoder: Encoder, value: User) {
        when (value) {
            is User.Timeline -> encoder.encodeSerializableValue(User.Timeline.serializer(), value)
            is User.UserScreen -> encoder.encodeSerializableValue(
                User.UserScreen.serializer(),
                value
            )

            is User.Notification -> encoder.encodeSerializableValue(
                User.Notification.serializer(),
                value
            )
        }
    }

    override fun deserialize(decoder: Decoder): User {
        require(decoder is JsonDecoder)
        val element = decoder.decodeJsonElement()
        return when {
            element.jsonObject.containsKey("notesCount") -> decoder.json.decodeFromJsonElement(
                User.UserScreen.serializer(),
                element
            )

            !element.jsonObject.containsKey("notesCount") && element.jsonObject.containsKey(
                "requireSigninToViewContents"
            ) -> {
                decoder.json.decodeFromJsonElement(
                    User.Timeline.serializer(),
                    element
                )
            }

            !element.jsonObject.containsKey("notesCount") && !element.jsonObject.containsKey(
                "requireSigninToViewContents"
            ) -> {
                decoder.json.decodeFromJsonElement(
                    User.Notification.serializer(),
                    element
                )
            }

            else -> throw IllegalArgumentException("Unknown type of User")
        }
    }
}
