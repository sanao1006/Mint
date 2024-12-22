package me.sanao1006.core.model.uistate

sealed interface AnnouncementUiState {
    data object Loading : AnnouncementUiState
    data object Failed : AnnouncementUiState
    data class Success(
        val text: String,
        val title: String,
        val id: String,
        val imageUrl: String? = null,
        val createdAt: String,
        val display: String,
        val forYou: Boolean,
        val icon: String,
        val updatedAt: String? = null,
    ) : AnnouncementUiState
}
