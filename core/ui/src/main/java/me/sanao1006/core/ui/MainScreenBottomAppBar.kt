package me.sanao1006.core.ui

import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.LocalContentColor
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import ir.alirezaivaz.tablericons.TablerIcons
import me.sanao1006.screens.MainScreenType
import me.sanao1006.screens.event.BottomAppBarActionEvent

@Composable
fun MainScreenBottomAppBarWrapper(
    mainScreenType: MainScreenType,
    modifier: Modifier = Modifier,
    event: (BottomAppBarActionEvent) -> Unit,
    floatingActionButton: @Composable () -> Unit
) = MainScreenBottomAppBar(
    mainSheetType = mainScreenType,
    onHomeClick = { event(BottomAppBarActionEvent.OnHomeIconClicked) },
    onSearchClick = { event(BottomAppBarActionEvent.OnSearchIconClicked) },
    onNotificationClick = { event(BottomAppBarActionEvent.OnNotificationIconClicked) },
    modifier = modifier,
    floatingActionButton = floatingActionButton
)

@Composable
private fun MainScreenBottomAppBar(
    mainSheetType: MainScreenType,
    onHomeClick: () -> Unit,
    onSearchClick: () -> Unit,
    onNotificationClick: () -> Unit,
    modifier: Modifier = Modifier,
    floatingActionButton: @Composable () -> Unit
) {
    BottomAppBar(
        modifier = modifier,
        floatingActionButton = floatingActionButton,
        actions = {
            BottomSheetType.entries.forEach {
                BottomNavigationItem(
                    icon = {
                        Icon(
                            painter = painterResource(it.resId),
                            contentDescription = null,
                            tint = if (it.mainSheetType == mainSheetType)
                                MaterialTheme.colorScheme.primary
                            else
                                LocalContentColor.current
                        )
                    },
                    selected = it.mainSheetType == mainSheetType,
                    onClick = {
                        if (it.mainSheetType != mainSheetType) {
                            when (it) {
                                BottomSheetType.HOME -> onHomeClick()
                                BottomSheetType.SEARCH -> onSearchClick()
                                BottomSheetType.NOTIFICATION -> onNotificationClick()
                            }
                        }
                    }
                )
            }
        }
    )
}

private enum class BottomSheetType(
    val mainSheetType: MainScreenType,
    val resId: Int
) {
    HOME(MainScreenType.HOME, TablerIcons.Home),
    SEARCH(MainScreenType.SEARCH, TablerIcons.Search),
    NOTIFICATION(MainScreenType.NOTIFICATION, TablerIcons.Bell)
}
