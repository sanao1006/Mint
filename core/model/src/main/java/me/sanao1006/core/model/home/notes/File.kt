package me.sanao1006.core.model.home.notes


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class File(
    @SerialName("blurhash")
    val blurhash: String = "",
    @SerialName("comment")
    val comment: String = "",
    @SerialName("createdAt")
    val createdAt: String = "",
    @SerialName("folder")
    val folder: Folder? = Folder(),
    @SerialName("folderId")
    val folderId: String = "",
    @SerialName("id")
    val id: String = "",
    @SerialName("isSensitive")
    val isSensitive: Boolean = false,
    @SerialName("md5")
    val md5: String = "",
    @SerialName("name")
    val name: String = "",
    @SerialName("properties")
    val properties: Properties? = Properties(),
    @SerialName("size")
    val size: Int = 0,
    @SerialName("thumbnailUrl")
    val thumbnailUrl: String = "",
    @SerialName("type")
    val type: String = "",
    @SerialName("url")
    val url: String = "",
    @SerialName("user")
    val user: User? = User(),
    @SerialName("userId")
    val userId: String = ""
)