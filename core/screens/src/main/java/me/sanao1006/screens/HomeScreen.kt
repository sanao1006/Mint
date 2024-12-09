package me.sanao1006.screens

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material3.DrawerState
import androidx.compose.runtime.Immutable
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.screen.Screen
import kotlinx.coroutines.CoroutineScope
import kotlinx.parcelize.Parcelize
import me.sanao1006.core.model.LoginUserInfo
import me.sanao1006.core.model.notes.TimelineUiState

@Parcelize
data object HomeScreen : Screen {
    @OptIn(ExperimentalMaterialApi::class)
    @Immutable
    data class State(
        val uiState: List<TimelineUiState?> = listOf(),
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

        data object OnNoteCreateClicked : Event()
        data class OnNavigationIconClicked(
            val drawerState: DrawerState,
            val scope: CoroutineScope
        ) : Event()

        sealed class DrawerEvent : Event() {
            data object OnDrawerFavoriteClicked : DrawerEvent()
            data object OnDrawerAnnouncementClicked : DrawerEvent()
            data object OnDrawerClipClicked : DrawerEvent()
            data object OnDrawerAntennaClicked : DrawerEvent()
            data object OnDrawerExploreClicked : DrawerEvent()
            data object OnDrawerChannelClicked : DrawerEvent()
            data object OnDrawerDriveClicked : DrawerEvent()
            data object OnDrawerAboutClicked : DrawerEvent()
            data object OnDrawerAccountPreferencesClicked : DrawerEvent()
            data object OnDrawerSettingsClicked : DrawerEvent()
            data object OnDrawerIconClicked : DrawerEvent()
            data object OnDrawerFollowingCountClicked : DrawerEvent()
            data object OnDrawerFollowersCountClicked : DrawerEvent()
        }

        sealed class TimelineEvent : Event() {
            data object OnLocalTimelineClicked : TimelineEvent()
            data object OnSocialTimelineClicked : TimelineEvent()
            data object OnGlobalTimelineClicked : TimelineEvent()
        }

        sealed class TimelineItemEvent : Event() {
            abstract val userId: String
            abstract val userName: String?
            abstract val host: String?

            data class OnTimelineIconClicked(
                override val userId: String,
                override val userName: String?,
                override val host: String?
            ) : TimelineItemEvent()
        }

        sealed class BottomAppBarActionEvent : Event() {
            data object OnHomeIconClicked : BottomAppBarActionEvent()
            data object OnSearchIconClicked : BottomAppBarActionEvent()
            data object OnNotificationIconClicked : BottomAppBarActionEvent()
        }
    }
}
