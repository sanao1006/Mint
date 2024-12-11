package me.sanao1006.core.ui

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.HorizontalFloatingAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import ir.alirezaivaz.tablericons.TablerIcons
import me.sanao1006.screens.MainScreenType
import me.sanao1006.screens.event.BottomAppBarActionEvent

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun MainScreenBottomAppBarWrapper(
    mainScreenType: MainScreenType,
    modifier: Modifier = Modifier,
    event: (BottomAppBarActionEvent) -> Unit,
    floatingActionButton: @Composable RowScope.() -> Unit
) = MainScreenBottomAppBar(
    mainSheetType = mainScreenType,
    onHomeClick = { event(BottomAppBarActionEvent.OnHomeIconClicked) },
    onNotificationClick = { event(BottomAppBarActionEvent.OnNotificationIconClicked) },
    modifier = modifier,
    floatingActionButton = floatingActionButton
)

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun MainScreenBottomAppBar(
    mainSheetType: MainScreenType,
    onHomeClick: () -> Unit,
    onNotificationClick: () -> Unit,
    modifier: Modifier = Modifier,
    floatingActionButton: @Composable RowScope.() -> Unit = {}
) {
    HorizontalFloatingAppBar(
        expanded = true,
        modifier = modifier,
        leadingContent = {
            IconButton(
                modifier = Modifier.padding(start = 8.dp, end = 16.dp),
                onClick = onHomeClick
            ) {
                Icon(
                    painter = painterResource(
                        if (mainSheetType == MainScreenType.HOME)
                            TablerIcons.HomeFilled
                        else
                            TablerIcons.Home
                    ),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        },
        trailingContent = {
            IconButton(
                modifier = Modifier.padding(start = 16.dp, end = 8.dp),
                onClick = onNotificationClick
            ) {
                Icon(
                    painter = painterResource(
                        if (mainSheetType == MainScreenType.NOTIFICATION)
                            TablerIcons.BellFilled
                        else
                            TablerIcons.Bell
                    ),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        },
        content = floatingActionButton
    )
}

private enum class MainScreenType {
    HOME,
    NOTIFICATION
}
