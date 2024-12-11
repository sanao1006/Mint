package me.sanao1006.core.ui

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.DrawerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter
import ir.alirezaivaz.tablericons.TablerIcons
import me.sanao1006.core.model.LoginUserInfo
import me.sanao1006.screens.event.DrawerEvent
import me.snao1006.res_value.ResString

@Composable
fun MainScreenDrawerWrapper(
    loginUserInfo: LoginUserInfo,
    drawerState: DrawerState,
    event: (DrawerEvent) -> Unit,
    content: @Composable () -> Unit
) = MainScreenDrawer(
    loginUserInfo = loginUserInfo,
    drawerState = drawerState,
    onDrawerFavoriteClick = { event(DrawerEvent.OnDrawerFavoriteClicked) },
    onDrawerAnnouncementClick = { event(DrawerEvent.OnDrawerAnnouncementClicked) },
    onDrawerClipClick = { event(DrawerEvent.OnDrawerClipClicked) },
    onDrawerAntennaClick = { event(DrawerEvent.OnDrawerAntennaClicked) },
    onDrawerExploreClick = { event(DrawerEvent.OnDrawerExploreClicked) },
    onDrawerChannelClick = { event(DrawerEvent.OnDrawerChannelClicked) },
    onDrawerSearchClick = { event(DrawerEvent.OnDrawerSearchClicked) },
    onDrawerDriveClick = { event(DrawerEvent.OnDrawerDriveClicked) },
    onDrawerAboutClick = { event(DrawerEvent.OnDrawerAboutClicked) },
    onDrawerAccountPreferencesClick = { event(DrawerEvent.OnDrawerAccountPreferencesClicked) },
    onDrawerSettingsClick = { event(DrawerEvent.OnDrawerSettingsClicked) },
    onIconClick = { event(DrawerEvent.OnDrawerIconClicked) },
    onFollowingCountClick = { event(DrawerEvent.OnDrawerFollowingCountClicked) },
    onFollowersCountClick = { event(DrawerEvent.OnDrawerFollowersCountClicked) },
    content = content
)

@Composable
private fun MainScreenDrawer(
    loginUserInfo: LoginUserInfo,
    modifier: Modifier = Modifier,
    drawerState: DrawerState,
    onDrawerFavoriteClick: () -> Unit,
    onDrawerAnnouncementClick: () -> Unit,
    onDrawerClipClick: () -> Unit,
    onDrawerAntennaClick: () -> Unit,
    onDrawerExploreClick: () -> Unit,
    onDrawerChannelClick: () -> Unit,
    onDrawerSearchClick: () -> Unit,
    onDrawerDriveClick: () -> Unit,
    onDrawerAboutClick: () -> Unit,
    onDrawerAccountPreferencesClick: () -> Unit,
    onDrawerSettingsClick: () -> Unit,
    onIconClick: () -> Unit,
    onFollowingCountClick: () -> Unit,
    onFollowersCountClick: () -> Unit,
    content: @Composable () -> Unit
) {
    ModalNavigationDrawer(
        modifier = modifier,
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                LazyColumn {
                    item {
                        LoginUserInfoBox(
                            modifier = Modifier.padding(start = 16.dp, end = 24.dp),
                            loginUserInfo = loginUserInfo,
                            onIconClick = onIconClick,
                            onFollowingCountClick = onFollowingCountClick,
                            onFollowersCountClick = onFollowersCountClick
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        HorizontalDivider(
                            modifier = Modifier
                                .fillMaxWidth(0.9f)
                                .align(Alignment.CenterHorizontally)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                    items(DrawerItem.entries) {
                        NavigationDrawerItem(
                            icon = { Icon(painter = painterResource(it.iconId), "") },
                            label = { Text(text = stringResource(it.titleId)) },
                            selected = false,
                            onClick = when (it) {
                                DrawerItem.FAVORITE -> onDrawerFavoriteClick
                                DrawerItem.ANNOUNCEMENT -> onDrawerAnnouncementClick
                                DrawerItem.CLIP -> onDrawerClipClick
                                DrawerItem.ANTENNA -> onDrawerAntennaClick
                                DrawerItem.EXPLORE -> onDrawerExploreClick
                                DrawerItem.CHANNEL -> onDrawerChannelClick
                                DrawerItem.SEARCH -> onDrawerSearchClick
                                DrawerItem.DRIVE -> onDrawerDriveClick
                                DrawerItem.ABOUT -> onDrawerAboutClick
                                DrawerItem.ACCOUNT_PREFERENCES -> onDrawerAccountPreferencesClick
                                DrawerItem.SETTINGS -> onDrawerSettingsClick
                            }
                        )
                    }
                }
            }
        },
        content = content
    )
}

@Composable
private fun LoginUserInfoBox(
    loginUserInfo: LoginUserInfo,
    modifier: Modifier = Modifier,
    onIconClick: () -> Unit = {},
    onFollowingCountClick: () -> Unit,
    onFollowersCountClick: () -> Unit = {}
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.Start
    ) {
        Spacer(modifier = Modifier.height(48.dp))
        Image(
            painter = rememberAsyncImagePainter(loginUserInfo.avatarUrl),
            contentDescription = null,
            modifier = Modifier
                .clip(CircleShape)
                .size(64.dp)
                .clickable { onIconClick() },
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = loginUserInfo.userName,
            style = MaterialTheme.typography.headlineSmall
        )
        if (loginUserInfo.name.isNotEmpty()) {
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = loginUserInfo.name,
                style = MaterialTheme.typography.labelSmall
            )
        }
        Spacer(modifier = Modifier.height(8.dp))

        UserFollowInfo(
            followingCount = loginUserInfo.followingCount,
            followersCount = loginUserInfo.followersCount,
            onFollowingCountClick = onFollowingCountClick,
            onFollowersCountClick = onFollowersCountClick
        )
    }
}

@Composable
private fun UserFollowInfo(
    followingCount: Int,
    followersCount: Int,
    onFollowingCountClick: () -> Unit,
    onFollowersCountClick: () -> Unit
) {
    Row(
        modifier = Modifier.wrapContentHeight(align = Alignment.Bottom),
        verticalAlignment = Alignment.CenterVertically
    ) {
        FollowCount(
            count = followingCount,
            labelResId = ResString.drawer_following,
            onClick = onFollowingCountClick
        )
        Spacer(modifier = Modifier.width(10.dp))
        FollowCount(
            count = followersCount,
            labelResId = ResString.drawer_followers,
            onClick = onFollowersCountClick
        )
    }
}

@Composable
private fun FollowCount(count: Int, labelResId: Int, onClick: () -> Unit) {
    Row(modifier = Modifier.clickable { onClick() }) {
        Text(
            text = count.toString(),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = stringResource(labelResId),
            style = MaterialTheme.typography.labelLarge
        )
    }
}

enum class DrawerItem(@DrawableRes val iconId: Int, @StringRes val titleId: Int) {
    FAVORITE(TablerIcons.Star, ResString.drawer_item_favorite),
    ANNOUNCEMENT(TablerIcons.Speakerphone, ResString.drawer_item_announcement),
    CLIP(TablerIcons.Paperclip, ResString.drawer_item_clip),
    ANTENNA(TablerIcons.Antenna, ResString.drawer_item_antenna),
    EXPLORE(TablerIcons.Hash, ResString.drawer_item_explore),
    CHANNEL(TablerIcons.DeviceTv, ResString.drawer_item_channel),
    SEARCH(TablerIcons.Search, ResString.drawer_item_search),
    DRIVE(TablerIcons.BrandOnedrive, ResString.drawer_item_drive),
    ABOUT(TablerIcons.InfoCircle, ResString.drawer_item_about),
    ACCOUNT_PREFERENCES(TablerIcons.User, ResString.drawer_item_account_preferences),
    SETTINGS(TablerIcons.Settings, ResString.drawer_item_settings);
}
