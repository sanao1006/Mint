package me.sanao1006.feature.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import compose.icons.TablerIcons
import compose.icons.tablericons.Atom2
import compose.icons.tablericons.Home
import compose.icons.tablericons.World

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenTopAppBar(
    modifier: Modifier = Modifier,
    onNavigationIconClick: () -> Unit,
    onHomeClick: () -> Unit,
    onSocialClick: () -> Unit,
    onGlobalClick: () -> Unit
) {
    TopAppBar(
        modifier = modifier,
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton(onClick = onHomeClick) {
                    Icon(imageVector = TablerIcons.Home, "Home")
                }

                IconButton(onClick = onSocialClick) {
                    Icon(imageVector = TablerIcons.World, "Social")
                }

                IconButton(onClick = onGlobalClick) {
                    Icon(imageVector = TablerIcons.Atom2, "Global")
                }
            }
        },
        navigationIcon = {
            IconButton(onClick = onNavigationIconClick) {
                Icon(imageVector = Icons.Default.Menu, "")
            }
        }
    )
}

@PreviewLightDark
@Composable
fun PreviewHomeScreenTopAppBar() {
    HomeScreenTopAppBar(
        onNavigationIconClick = {},
        onHomeClick = {},
        onSocialClick = {},
        onGlobalClick = {}
    )
}
