package me.sanao1006.core.model.notes


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class File(
    @SerialName("blurhash")
    val blurhash: String? = null,
    @SerialName("comment")
    val comment: String? = null,
    @SerialName("createdAt")
    val createdAt: String = "",
    @SerialName("folder")
    val folder: me.sanao1006.core.model.notes.Folder? = me.sanao1006.core.model.notes.Folder(),
    @SerialName("folderId")
    val folderId: String? = null,
    @SerialName("id")
    val id: String = "",
    @SerialName("isSensitive")
    val isSensitive: Boolean = false,
    @SerialName("md5")
    val md5: String = "",
    @SerialName("name")
    val name: String = "",
    @SerialName("properties")
    val properties: me.sanao1006.core.model.notes.Properties? = me.sanao1006.core.model.notes.Properties(),
    @SerialName("size")
    val size: Int = 0,
    @SerialName("thumbnailUrl")
    val thumbnailUrl: String? = null,
    @SerialName("type")
    val type: String = "",
    @SerialName("url")
    val url: String = "",
    @SerialName("user")
    val user: me.sanao1006.core.model.notes.User? = me.sanao1006.core.model.notes.User(),
    @SerialName("userId")
    val userId: String? = null
)