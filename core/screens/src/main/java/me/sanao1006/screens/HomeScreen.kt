package me.sanao1006.screens

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.runtime.Immutable
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.screen.Screen
import kotlinx.coroutines.CoroutineScope
import kotlinx.parcelize.Parcelize
import me.sanao1006.core.model.LoginUserInfo

@Parcelize
data object HomeScreen : Screen {
    @Immutable
    data class State @OptIn(ExperimentalMaterialApi::class) constructor(
        val uiState: List<me.sanao1006.core.model.notes.TimelineUiState?> = listOf(),
        val navigator: Navigator,
        val isSuccessCreateNote: Boolean? = null,
        val pullToRefreshState: PullRefreshState,
        val isRefreshed: Boolean = false,
        val drawerUserInfo: LoginUserInfo,
        val eventSink: (Event) -> Unit
    ) : CircuitUiState

    sealed class Event : CircuitUiEvent {
        data class OnNoteCreated(
            val snackbarHostState: SnackbarHostState,
            val scope: CoroutineScope
        ) : Event()

        data object OnLocalTimelineClicked : Event()
        data object OnSocialTimelineClicked : Event()
        data object OnGlobalTimelineClicked : Event()
        data object OnNoteCreateClicked : Event()

        data object OnDrawerFavoriteClicked : Event()
        data object OnDrawerAnnouncementClicked : Event()
        data object OnDrawerClipClicked : Event()
        data object OnDrawerAntennaClicked : Event()
        data object OnDrawerExploreClicked : Event()
        data object OnDrawerChannelClicked : Event()
        data object OnDrawerDriveClicked : Event()
        data object OnDrawerGalleryClicked : Event()
        data object OnDrawerAboutClicked : Event()
        data object OnDrawerAccountPreferencesClicked : Event()
        data object OnDrawerSettingsClicked : Event()
    }
}
