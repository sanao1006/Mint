package me.sanao1006.core.ui

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import ir.alirezaivaz.tablericons.TablerIcons
import me.snao1006.res_value.ResString

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrawerItemScreenWrapper(
    drawerItem: DrawerItem,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    onBackIconClick: () -> Unit,
    actions: @Composable RowScope.() -> Unit = { },
    content: @Composable (Modifier) -> Unit
) {
    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        stringResource(
                            when (drawerItem) {
                                DrawerItem.FAVORITE -> ResString.drawer_item_favorite
                                DrawerItem.ANNOUNCEMENT -> ResString.drawer_item_announcement
                                DrawerItem.ANTENNA -> ResString.drawer_item_antenna
                                DrawerItem.EXPLORE -> ResString.drawer_item_explore
                                DrawerItem.CHANNEL -> ResString.drawer_item_channel
                                DrawerItem.SEARCH -> ResString.drawer_item_search
                                DrawerItem.DRIVE -> ResString.drawer_item_drive
                                DrawerItem.ACCOUNT_PREFERENCES ->
                                    ResString.drawer_item_account_preferences

                                DrawerItem.SETTINGS -> ResString.drawer_item_settings
                            }
                        )
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBackIconClick
                    ) {
                        Icon(
                            painter = painterResource(TablerIcons.ArrowLeft),
                            contentDescription = null
                        )
                    }
                },
                actions = actions
            )
        }
    ) {
        content(Modifier.padding(it))
    }
}
