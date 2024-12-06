package me.sanao1006.feature.home

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
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
import me.snao1006.res_value.ResString

@Composable
fun HomeScreenDrawer(
    loginUserInfo: LoginUserInfo,
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
                LoginUserInfo(
                    modifier = Modifier.padding(start = 16.dp, end = 24.dp),
                    loginUserInfo = loginUserInfo
                )
                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(
                    modifier = Modifier
                      .fillMaxWidth(0.9f)
                      .align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.height(16.dp))
                DrawerItem.entries.forEach {
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

@Composable
private fun LoginUserInfo(
    loginUserInfo: LoginUserInfo,
    modifier: Modifier = Modifier
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
              .size(64.dp),
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

        Row(
            modifier = Modifier.wrapContentHeight(align = Alignment.Bottom),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = loginUserInfo.followingCount.toString(),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = stringResource(ResString.drawer_following),
                style = MaterialTheme.typography.labelLarge
            )

            Spacer(modifier = Modifier.width(10.dp))

            Text(
                text = loginUserInfo.followersCount.toString(),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = stringResource(ResString.drawer_followers),
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}

enum class DrawerItem(@DrawableRes val iconId: Int, @StringRes val titleId: Int) {
    FAVORITE(TablerIcons.Star, ResString.drawer_item_favorite),
    ANNOUNCEMENT(TablerIcons.Speakerphone, ResString.drawer_item_announcement),
    CLIP(TablerIcons.Paperclip, ResString.drawer_item_clip),
    ANTENNA(TablerIcons.Antenna, ResString.drawer_item_antenna),
    EXPLORE(TablerIcons.Hash, ResString.drawer_item_explore),
    CHANNEL(TablerIcons.DeviceTv, ResString.drawer_item_channel),
    DRIVE(TablerIcons.BrandOnedrive, ResString.drawer_item_drive),
    GALLERY(TablerIcons.Icons, ResString.drawer_item_gallery),
    ABOUT(TablerIcons.InfoCircle, ResString.drawer_item_about),
    ACCOUNT_PREFERENCES(TablerIcons.User, ResString.drawer_item_account_preferences),
    SETTINGS(TablerIcons.Settings, ResString.drawer_item_settings);
}
