package me.sanao1006.core.model.uistate

import me.sanao1006.core.model.meta.Announcement

sealed interface AnnouncementUiState {
    data object Loading : AnnouncementUiState
    data object Failed : AnnouncementUiState
    data class Success(
        val announcements: List<Announcement>
    ) : AnnouncementUiState
}
