package me.sanao1006.feature.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import ir.alirezaivaz.tablericons.TablerIcons
import me.sanao1006.core.ui.modifier.bottomBorder

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
    TopAppBar(
        scrollBehavior = scrollBehavior,
        modifier = modifier,
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TopAppBarTimelineState.entries.forEach { state ->
                    IconButton(
                        modifier = Modifier.then(
                            if (state == topAppBarTimelineState) Modifier.bottomBorder()
                            else { Modifier }
                        ),
                        onClick = {
                            when (state) {
                                TopAppBarTimelineState.HOME -> onHomeClick()
                                TopAppBarTimelineState.SOCIAL -> onSocialClick()
                                TopAppBarTimelineState.GLOBAL -> onGlobalClick()
                            }
                        }
                    ) {
                        Icon(
                            painter = painterResource(
                                when (state) {
                                    TopAppBarTimelineState.HOME -> TablerIcons.Home
                                    TopAppBarTimelineState.SOCIAL -> TablerIcons.Planet
                                    TopAppBarTimelineState.GLOBAL -> TablerIcons.Universe
                                }
                            ),
                            contentDescription = state.name
                        )
                    }
                }
            }
        },
        navigationIcon = {
            IconButton(onClick = onNavigationIconClick) {
                Icon(painter = painterResource(TablerIcons.Menu2), "")
            }
        }
    )
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
fun PreviewHomeScreenTopAppBar() {
    HomeScreenTopAppBar(
        topAppBarTimelineState = TopAppBarTimelineState.SOCIAL,
        scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
        onNavigationIconClick = {},
        onHomeClick = {},
        onSocialClick = {},
        onGlobalClick = {}
    )
}
