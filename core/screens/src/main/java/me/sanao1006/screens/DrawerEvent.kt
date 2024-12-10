package me.sanao1006.screens

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
