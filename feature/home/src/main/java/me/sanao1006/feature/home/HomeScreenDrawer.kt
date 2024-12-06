package me.sanao1006.feature.home

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.DrawerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import ir.alirezaivaz.tablericons.TablerIcons

@Composable
fun HomeScreenDrawer(
    modifier: Modifier = Modifier,
    drawerState: DrawerState,
    onDrawerFavoriteClick: () -> Unit,
    onDrawerAnnouncementClick: () -> Unit,
    onDrawerClipClick: () -> Unit,
    onDrawerAntennaClick: () -> Unit,
    onDrawerExploreClick: () -> Unit,
    onDrawerChannelClick: () -> Unit,
    onDrawerDriveClick: () -> Unit,
    onDrawerGalleryClick: () -> Unit,
    onDrawerAboutClick: () -> Unit,
    onDrawerAccountPreferencesClick: () -> Unit,
    onDrawerSettingsClick: () -> Unit,
    content: @Composable () -> Unit
) {
    ModalNavigationDrawer(
        modifier = modifier,
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.height(16.dp))
                DrawerItem.entries.forEach {
                    NavigationDrawerItem(
                        icon = { Icon(painter = painterResource(it.resId), "") },
                        label = { Text(it.title) },
                        selected = false,
                        onClick = when (it) {
                            DrawerItem.FAVORITE -> onDrawerFavoriteClick
                            DrawerItem.ANNOUNCEMENT -> onDrawerAnnouncementClick
                            DrawerItem.CLIP -> onDrawerClipClick
                            DrawerItem.ANTENNA -> onDrawerAntennaClick
                            DrawerItem.EXPLORE -> onDrawerExploreClick
                            DrawerItem.CHANNEL -> onDrawerChannelClick
                            DrawerItem.DRIVE -> onDrawerDriveClick
                            DrawerItem.GALLERY -> onDrawerGalleryClick
                            DrawerItem.ABOUT -> onDrawerAboutClick
                            DrawerItem.ACCOUNT_PREFERENCES -> onDrawerAccountPreferencesClick
                            DrawerItem.SETTINGS -> onDrawerSettingsClick
                        }
                    )
                }
            }
        },
        content = content
    )
}

enum class DrawerItem(val resId: Int, val title: String) {
    FAVORITE(TablerIcons.Star, "Favorite"),
    ANNOUNCEMENT(TablerIcons.Speakerphone, "Announcement"),
    CLIP(TablerIcons.Paperclip, "Clip"),
    ANTENNA(TablerIcons.Antenna, "Antenna"),
    EXPLORE(TablerIcons.Hash, "Explore"),
    CHANNEL(TablerIcons.DeviceTv, "Channel"),
    DRIVE(TablerIcons.BrandOnedrive, "Drive"),
    GALLERY(TablerIcons.Icons, "Gallery"),
    ABOUT(TablerIcons.InfoCircle, "About"),
    ACCOUNT_PREFERENCES(TablerIcons.User, "Account Preferences"),
    SETTINGS(TablerIcons.Settings, "Settings")
}
