package me.sanao1006.feature.channel

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.slack.circuit.codegen.annotations.CircuitInject
import dagger.hilt.components.SingletonComponent
import ir.alirezaivaz.tablericons.TablerIcons
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import me.sanao1006.core.designsystem.MintTheme
import me.sanao1006.core.model.notes.Channel
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
                channelList = uiState.channelList,
                channelText = uiState.channelName,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                isSearchTab = page == 0,
                onEnterClick = onEnterClick,
                onValueChange = onValueChange
            )
        }
    }
}

@Composable
private fun ChannelListView(
    channelList: List<Channel>,
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
        ChannelListColumn(
            channelList = channelList,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun ChannelListColumn(
    channelList: List<Channel>,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        items(channelList) {
            ChannelCard(channel = it)
        }
    }
}

@Composable
private fun ChannelCard(
    channel: Channel,
    modifier: Modifier = Modifier,
    onFollowButtonClick: () -> Unit = {},
    onCardClick: () -> Unit = {}
) {
    ElevatedCard(
        modifier = modifier,
        onClick = onCardClick
    ) {
        Column {
            Box(modifier = Modifier.fillMaxSize()) {
                if (channel.bannerUrl != null) {
                    AsyncImage(
                        model = channel.bannerUrl,
                        contentDescription = "",
                        modifier = Modifier
                            .height(160.dp)
                            .fillMaxWidth(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .height(160.dp)
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.outline)
                    )
                }
                Button(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(8.dp),
                    onClick = onFollowButtonClick
                ) {
                    Icon(
                        modifier = Modifier.size(18.dp),
                        painter = painterResource(
                            if (channel.isFollowing) {
                                TablerIcons.CircleMinus
                            } else {
                                TablerIcons.CirclePlus
                            }
                        ),
                        contentDescription = ""
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = stringResource(
                            if (channel.isFollowing) {
                                ResString.unfollow_description
                            } else {
                                ResString.follow_description
                            }
                        )
                    )
                }
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(8.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(
                            MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.9f)
                        )
                        .padding(4.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(TablerIcons.Users),
                            contentDescription = "",
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = stringResource(
                                ResString.count_participants,
                                channel.usersCount.toString()
                            )
                        )
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(TablerIcons.Pencil),
                            contentDescription = "",
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = stringResource(
                                ResString.count_notes,
                                channel.notesCount.toString()
                            )
                        )
                    }
                }
            }
            Column(modifier = Modifier.padding(8.dp)) {
                Text(
                    text = channel.name,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = channel.description ?: "",
                    maxLines = 5,
                    overflow = TextOverflow.Ellipsis
                )
            }
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
