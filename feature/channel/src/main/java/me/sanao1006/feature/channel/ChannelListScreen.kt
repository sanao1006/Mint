package me.sanao1006.feature.channel

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.slack.circuit.codegen.annotations.CircuitInject
import dagger.hilt.components.SingletonComponent
import ir.alirezaivaz.tablericons.TablerIcons
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import me.sanao1006.core.designsystem.MintTheme
import me.sanao1006.core.model.uistate.ChannelListUiState
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
        val pagerState = rememberPagerState(initialPage = 0) { 3 }
        val scope = rememberCoroutineScope()
        LaunchedEffect(pagerState) {
            snapshotFlow { pagerState.currentPage }.collectLatest { page ->
                state.eventSink(ChannelListScreen.Event.OnPageChange(page))
            }
        }

        DrawerItemScreenWrapper(
            drawerItem = DrawerItem.CHANNEL,
            snackbarHostState = snackbarHostState,
            onBackIconClick = {
                state.globalIconEventSink(GlobalIconEvent.OnBackBeforeScreen)
            }
        ) {
            Column(modifier = it.fillMaxSize()) {
                ChannelListScreenUiContent(
                    uiState = state.channelListUiState,
                    pagerState = pagerState,
                    modifier = Modifier.fillMaxWidth(),
                    onSearchClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(0)
                            state.eventSink(ChannelListScreen.Event.OnSearchClick)
                        }
                    },
                    onTrendClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(1)
                            state.eventSink(ChannelListScreen.Event.OnTrendClick)
                        }
                    },
                    onFavoriteClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(2)
                            state.eventSink(ChannelListScreen.Event.OnFavoriteClick)
                        }
                    },
                    onEnterClick = {
                        state.eventSink(ChannelListScreen.Event.OnEnterClick)
                    },
                    onValueChange = { channelName ->
                        state.eventSink(ChannelListScreen.Event.OnChannelNameChange(channelName))
                    }
                )
            }
        }
    }
}

@Composable
private fun ChannelListScreenUiContent(
    uiState: ChannelListUiState,
    pagerState: PagerState,
    modifier: Modifier = Modifier,
    onSearchClick: () -> Unit,
    onTrendClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    onEnterClick: () -> Unit,
    onValueChange: (String) -> Unit
) {
    Column(modifier = modifier) {
        TabRow(selectedTabIndex = uiState.selectedTabIndex) {
            Tab(
                selected = uiState.selectedTabIndex == 0,
                onClick = onSearchClick,
                text = {
                    TabIcon(
                        icon = TablerIcons.Search,
                        text = stringResource(ResString.search_description),
                        isSelected = uiState.selectedTabIndex == 0
                    )
                }
            )
            Tab(
                selected = uiState.selectedTabIndex == 1,
                onClick = onTrendClick,
                text = {
                    TabIcon(
                        icon = TablerIcons.Comet,
                        text = stringResource(ResString.trend_description),
                        isSelected = uiState.selectedTabIndex == 1
                    )
                }
            )
            Tab(
                selected = uiState.selectedTabIndex == 2,
                onClick = onFavoriteClick,
                text = {
                    TabIcon(
                        icon = TablerIcons.Star,
                        text = stringResource(ResString.favorite_description),
                        isSelected = uiState.selectedTabIndex == 2
                    )
                }
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        HorizontalPager(
            state = pagerState
        ) { page ->
            ChannelListView(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                channelText = uiState.channelName,
                isSearchTab = page == 0,
                onEnterClick = onEnterClick,
                onValueChange = onValueChange
            )
        }
    }
}

@Composable
private fun ChannelListView(
    channelText: String,
    isSearchTab: Boolean,
    modifier: Modifier = Modifier,
    onEnterClick: () -> Unit,
    onValueChange: (String) -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (isSearchTab) {
            TextField(
                value = channelText,
                onValueChange = onValueChange,
                maxLines = 1,
                leadingIcon = {
                    Icon(
                        painter = painterResource(TablerIcons.Search),
                        contentDescription = ""
                    )
                },
                label = { Text(text = stringResource(ResString.search_description)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(8.dp)),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(
                    onSearch = { onEnterClick() }
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
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
            ChannelListScreenUiContent(
                uiState = ChannelListUiState(),
                pagerState = rememberPagerState(initialPage = 0) { 3 },
                onSearchClick = {},
                onTrendClick = {},
                onFavoriteClick = {},
                onEnterClick = {},
                onValueChange = {}
            )
        }
    }
}
