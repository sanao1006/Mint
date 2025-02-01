package me.sanao1006.feature.channel

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.slack.circuit.codegen.annotations.CircuitInject
import dagger.hilt.components.SingletonComponent
import ir.alirezaivaz.tablericons.TablerIcons
import me.sanao1006.core.designsystem.MintTheme
import me.sanao1006.core.ui.DrawerItem
import me.sanao1006.core.ui.DrawerItemScreenWrapper
import me.sanao1006.screens.ChannelListScreen
import me.sanao1006.screens.event.globalIcon.GlobalIconEvent
import me.snao1006.res_value.ResString

@CircuitInject(ChannelListScreen::class, SingletonComponent::class)
@Composable
fun ChannelListScreen(state: ChannelListScreen.State, modifier: Modifier) {
    Box(modifier = modifier.fillMaxSize()) {
        val snackbarHostState = remember { SnackbarHostState() }
        DrawerItemScreenWrapper(
            drawerItem = DrawerItem.CHANNEL,
            snackbarHostState = snackbarHostState,
            onBackIconClick = {
                state.globalIconEventSink(GlobalIconEvent.OnBackBeforeScreen)
            }
        ) {
            Column(modifier = it.fillMaxSize()) {
                ChannelListScreenUiContent(
                    selectTabIndex = state.selectTabIndex,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
private fun ChannelListScreenUiContent(
    selectTabIndex: Int,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        TabRow(selectedTabIndex = selectTabIndex) {
            Tab(
                selected = selectTabIndex == 0,
                onClick = {},
                text = {
                    TabIcon(
                        icon = TablerIcons.Search,
                        text = stringResource(ResString.search_description),
                        isSelected = selectTabIndex == 0
                    )
                }
            )
            Tab(
                selected = selectTabIndex == 1,
                onClick = {},
                text = {
                    TabIcon(
                        icon = TablerIcons.Comet,
                        text = stringResource(ResString.trend_description),
                        isSelected = selectTabIndex == 1
                    )
                }
            )
            Tab(
                selected = selectTabIndex == 2,
                onClick = {},
                text = {
                    TabIcon(
                        icon = TablerIcons.Star,
                        text = stringResource(ResString.favorite_description),
                        isSelected = selectTabIndex == 2
                    )
                }
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
private fun TabIcon(
    icon: Int,
    text: String,
    isSelected: Boolean
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.wrapContentWidth()
    ) {
        Icon(
            modifier = Modifier.size(18.dp),
            painter = painterResource(icon),
            contentDescription = "",
            tint = if (isSelected) {
                LocalContentColor.current
            } else {
                Color.Unspecified
            }
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = text,
            color = if (isSelected) {
                Color.Unspecified
            } else {
                MaterialTheme.colorScheme.onSurface
            }
        )
    }
}

@PreviewLightDark
@Composable
fun ChannelListScreenPreview() {
    MintTheme {
        Surface {
            ChannelListScreenUiContent(selectTabIndex = 0)
        }
    }
}
