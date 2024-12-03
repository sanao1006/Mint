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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenTopAppBar(
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
                IconButton(onClick = onHomeClick) {
                    Icon(painter = painterResource(TablerIcons.Home), "Home")
                }

                IconButton(onClick = onSocialClick) {
                    Icon(painter = painterResource(TablerIcons.Planet), "Social")
                }

                IconButton(onClick = onGlobalClick) {
                    Icon(painter = painterResource(TablerIcons.Universe), "Global")
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

@OptIn(ExperimentalMaterial3Api::class)
@PreviewLightDark
@Composable
fun PreviewHomeScreenTopAppBar() {
    HomeScreenTopAppBar(
        scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
        onNavigationIconClick = {},
        onHomeClick = {},
        onSocialClick = {},
        onGlobalClick = {}
    )
}
