package me.sanao1006.feature.home

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.foundation.rememberAnsweringNavigator
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuitx.effects.LaunchedImpressionEffect
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.sanao1006.core.domain.home.GetNotesTimelineUseCase
import me.sanao1006.core.domain.home.TimelineType
import me.sanao1006.core.domain.home.UpdateAccountUseCase
import me.sanao1006.core.model.LoginUserInfo
import me.sanao1006.core.model.notes.TimelineUiState
import me.sanao1006.screens.HomeScreen
import me.sanao1006.screens.NoteScreen
import me.sanao1006.screens.UserScreen
import me.snao1006.res_value.ResString

class HomeScreenPresenter @AssistedInject constructor(
    @Assisted private val navigator: Navigator,
    private val getNotesTimelineUseCase: GetNotesTimelineUseCase,
    private val updateMyAccountUseCase: UpdateAccountUseCase
) : Presenter<HomeScreen.State> {

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    override fun present(): HomeScreen.State {
        var isSuccessCreateNote: Boolean? by rememberRetained { mutableStateOf(null) }
        var loginUserInfo: LoginUserInfo by rememberRetained {
            mutableStateOf(
                LoginUserInfo()
            )
        }
        val context = LocalContext.current
        val scope = rememberCoroutineScope()
        val nav = rememberAnsweringNavigator<NoteScreen.Result>(navigator) { result ->
            isSuccessCreateNote = result.success
        }

        var timelineType by rememberRetained { mutableStateOf(TimelineType.SOCIAL) }
        var timelineUiState: List<TimelineUiState> by rememberRetained(timelineType) {
            mutableStateOf(emptyList())
        }

        var isRefreshed by remember { mutableStateOf(false) }
        val pullRefreshState = rememberPullRefreshState(
            refreshing = isRefreshed,
            onRefresh = {
                scope.launch {
                    isRefreshed = true
                    timelineUiState = getNotesTimelineUseCase(timelineType)
                    delay(1500L)
                    isRefreshed = false
                }
            },
            refreshThreshold = 50.dp,
            refreshingOffset = 50.dp
        )
        LaunchedImpressionEffect {
            timelineUiState = getNotesTimelineUseCase(timelineType)
            loginUserInfo = updateMyAccountUseCase()
        }

        LaunchedImpressionEffect(timelineType) {
            timelineUiState = getNotesTimelineUseCase(timelineType)
        }

        return HomeScreen.State(
            uiState = timelineUiState,
            navigator = navigator,
            isSuccessCreateNote = isSuccessCreateNote,
            pullToRefreshState = pullRefreshState,
            isRefreshed = isRefreshed,
            drawerUserInfo = loginUserInfo
        ) { event ->
            when (event) {
                is HomeScreen.Event.OnNoteCreated -> {
                    isSuccessCreateNote?.let { flg ->
                        event.scope.launch {
                            event.snackbarHostState.showSnackbar(
                                message = if (flg) {
                                    context.getString(ResString.post_result_message_success)
                                } else {
                                    context.getString(ResString.post_result_message_failed)
                                }
                            )
                        }
                    }
                }

                HomeScreen.Event.OnLocalTimelineClicked -> {
                    timelineType = TimelineType.LOCAL
                }

                HomeScreen.Event.OnSocialTimelineClicked -> {
                    timelineType = TimelineType.SOCIAL
                }

                HomeScreen.Event.OnGlobalTimelineClicked -> {
                    timelineType = TimelineType.GLOBAL
                }

                HomeScreen.Event.OnNoteCreateClicked -> {
                    nav.goTo(NoteScreen)
//                    navigator.goTo(NoteScreen)
                }

                is HomeScreen.Event.OnNavigationIconClicked -> {
                    event.scope.launch {
                        event.drawerState.apply {
                            if (isClosed) open() else close()
                        }
                    }
                }

                // Bottom App Bar Event Begin
                HomeScreen.Event.OnHomeIconClicked -> {
                    navigator.goTo(HomeScreen)
                }

                HomeScreen.Event.OnSearchIconClicked -> {
                    // TODO goto Search Screen
                }

                HomeScreen.Event.OnNotificationIconClicked -> {
                    // TODO goto Notification Screen
                }
                // Bottom App Bar Event End

                // Drawer Event Begin
                HomeScreen.Event.OnDrawerFavoriteClicked -> {}

                HomeScreen.Event.OnDrawerAnnouncementClicked -> {}

                HomeScreen.Event.OnDrawerClipClicked -> {}

                HomeScreen.Event.OnDrawerAntennaClicked -> {}

                HomeScreen.Event.OnDrawerExploreClicked -> {}

                HomeScreen.Event.OnDrawerChannelClicked -> {}

                HomeScreen.Event.OnDrawerDriveClicked -> {}

                HomeScreen.Event.OnDrawerAboutClicked -> {}

                HomeScreen.Event.OnDrawerAccountPreferencesClicked -> {}

                HomeScreen.Event.OnDrawerSettingsClicked -> {}

                HomeScreen.Event.OnDrawerIconClicked -> {
                    nav.goTo(
                        UserScreen(
                            userId = loginUserInfo.userId,
                            userName = loginUserInfo.userName,
                            host = loginUserInfo.host,
                            isFromDrawer = true
                        )
                    )
                }

                HomeScreen.Event.OnDrawerFollowingCountClicked -> {}

                HomeScreen.Event.OnDrawerFollowersCountClicked -> {}

                // Drawer Event End

                is HomeScreen.Event.OnTimelineIconClicked -> {
                    nav.goTo(
                        UserScreen(
                            userId = event.userId,
                            userName = event.userName,
                            host = event.host
                        )
                    )
                }


            }
        }
    }
}

@CircuitInject(HomeScreen::class, SingletonComponent::class)
@AssistedFactory
fun interface Factory {
    fun create(navigator: Navigator): HomeScreenPresenter
}
