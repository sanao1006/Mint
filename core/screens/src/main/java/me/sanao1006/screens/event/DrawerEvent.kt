package me.sanao1006.screens.event

import com.slack.circuit.runtime.Navigator
import me.sanao1006.core.model.LoginUserInfo
import me.sanao1006.screens.UserScreen

sealed class DrawerEvent {
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

        else -> {}
    }
}