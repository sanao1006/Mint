package me.sanao1006.feature.home

import android.os.Vibrator
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.core.content.getSystemService
import ir.alirezaivaz.tablericons.TablerIcons
import me.sanao1006.core.data.util.vibrate
import me.sanao1006.core.designsystem.MintTheme
import me.sanao1006.feature.home.TopAppBarTimelineState.values

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun HomeScreenTopAppBar(
    topAppBarTimelineState: TopAppBarTimelineState,
    scrollBehavior: TopAppBarScrollBehavior,
    modifier: Modifier = Modifier,
    onNavigationIconClick: () -> Unit,
    onHomeClick: () -> Unit,
    onSocialClick: () -> Unit,
    onGlobalClick: () -> Unit
) {
    val vibrator = LocalContext.current.getSystemService<Vibrator>()
    Column {
        TopAppBar(
            colors = TopAppBarDefaults.topAppBarColors().copy(
                containerColor = MaterialTheme.colorScheme.background,
                scrolledContainerColor = MaterialTheme.colorScheme.background
            ),
            scrollBehavior = scrollBehavior,
            modifier = modifier
                .fillMaxWidth(),

            title = {},
            navigationIcon = {
                IconButton(onClick = onNavigationIconClick) {
                    Icon(painter = painterResource(TablerIcons.Menu2), "")
                }
            }
        )
        TabRow(
            modifier = Modifier.fillMaxWidth(),
            selectedTabIndex = topAppBarTimelineState.index
        ) {
            TopAppBarTimelineState.entries.forEach { state ->
                Tab(
                    modifier = Modifier.fillMaxWidth(),
                    selected = state == topAppBarTimelineState,
                    onClick = {
                        vibrator?.vibrate()
                        when (state) {
                            TopAppBarTimelineState.HOME -> onHomeClick()
                            TopAppBarTimelineState.SOCIAL -> onSocialClick()
                            TopAppBarTimelineState.GLOBAL -> onGlobalClick()
                        }
                    }
                ) {
                    Row(
                        modifier = Modifier.padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(
                                when (state) {
                                    TopAppBarTimelineState.HOME -> TablerIcons.Home
                                    TopAppBarTimelineState.SOCIAL -> TablerIcons.Universe
                                    TopAppBarTimelineState.GLOBAL -> TablerIcons.Whirl
                                }
                            ),
                            contentDescription = state.name,
                            tint = if (state == topAppBarTimelineState) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                Color.Unspecified
                            }
                        )
                        if (state == topAppBarTimelineState) {
                            Spacer(modifier = Modifier.padding(horizontal = 4.dp))
                            Text(
                                text = state.name,
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.wrapContentHeight()
                            )
                        }
                    }
                }
            }
        }
    }
}

internal enum class TopAppBarTimelineState(val index: Int) {
    HOME(0),
    SOCIAL(1),
    GLOBAL(2);

    companion object {
        fun get(index: Int): TopAppBarTimelineState = values().first { it.index == index }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@PreviewLightDark
@Composable
private fun PreviewHomeScreenTopAppBar() {
    MintTheme {
        HomeScreenTopAppBar(
            topAppBarTimelineState = TopAppBarTimelineState.SOCIAL,
            scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(),
            onNavigationIconClick = {},
            onHomeClick = {},
            onSocialClick = {},
            onGlobalClick = {}
        )
    }
}
