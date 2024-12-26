package me.sanao1006.screens.event.drawer

import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.Navigator
import me.sanao1006.core.model.LoginUserInfo
import me.sanao1006.screens.AnnouncementScreen
import me.sanao1006.screens.FavoritesScreen
import me.sanao1006.screens.SearchScreen
import me.sanao1006.screens.UserScreen

sealed class DrawerEvent : CircuitUiEvent {
    data object OnDrawerFavoriteClicked : DrawerEvent()
    data object OnDrawerAnnouncementClicked : DrawerEvent()
    data object OnDrawerAntennaClicked : DrawerEvent()
    data object OnDrawerExploreClicked : DrawerEvent()
    data object OnDrawerChannelClicked : DrawerEvent()
    data object OnDrawerSearchClicked : DrawerEvent()
    data object OnDrawerDriveClicked : DrawerEvent()
    data object OnDrawerAccountPreferencesClicked : DrawerEvent()
    data object OnDrawerSettingsClicked : DrawerEvent()
    data object OnDrawerIconClicked : DrawerEvent()
    data object OnDrawerFollowingCountClicked : DrawerEvent()
    data object OnDrawerFollowersCountClicked : DrawerEvent()
}

fun DrawerEvent.handleDrawerEvent(
    navigator: Navigator,
    loginUserInfo: LoginUserInfo
) {
    when (this) {
        DrawerEvent.OnDrawerIconClicked -> {
            navigator.goTo(
                UserScreen(
                    userId = loginUserInfo.userId,
                    userName = loginUserInfo.userName,
                    host = loginUserInfo.host,
                    isFromDrawer = true
                )
            )
        }

        DrawerEvent.OnDrawerSearchClicked -> {
            navigator.goTo(SearchScreen)
        }

        DrawerEvent.OnDrawerAnnouncementClicked -> {
            navigator.goTo(AnnouncementScreen)
        }

        DrawerEvent.OnDrawerFavoriteClicked -> {
            navigator.goTo(FavoritesScreen)
        }

        else -> {}
    }
}
