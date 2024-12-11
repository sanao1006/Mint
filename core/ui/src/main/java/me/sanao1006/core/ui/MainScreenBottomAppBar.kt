package me.sanao1006.core.ui

import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarScrollBehavior
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import ir.alirezaivaz.tablericons.TablerIcons
import me.sanao1006.screens.MainScreenType
import me.sanao1006.screens.event.BottomAppBarActionEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreenBottomAppBarWrapper(
    mainScreenType: MainScreenType,
    scrollBehavior: BottomAppBarScrollBehavior,
    modifier: Modifier = Modifier,
    event: (BottomAppBarActionEvent) -> Unit
) = MainScreenBottomAppBar(
    mainSheetType = mainScreenType,
    scrollBehavior = scrollBehavior,
    onHomeClick = { event(BottomAppBarActionEvent.OnHomeIconClicked) },
    onSearchClick = { event(BottomAppBarActionEvent.OnSearchIconClicked) },
    onNotificationClick = { event(BottomAppBarActionEvent.OnNotificationIconClicked) },
    modifier = modifier
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MainScreenBottomAppBar(
    mainSheetType: MainScreenType,
    scrollBehavior: BottomAppBarScrollBehavior,
    onHomeClick: () -> Unit,
    onSearchClick: () -> Unit,
    onNotificationClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    BottomAppBar(
        modifier = modifier,
        scrollBehavior = scrollBehavior
    ) {
        BottomSheetType.entries.forEach {
            NavigationBarItem(
                icon = {
                    Icon(
                        painter = painterResource(it.resId),
                        contentDescription = null,
                        tint = if (it.mainSheetType == mainSheetType) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            LocalContentColor.current
                        }
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
}

private enum class BottomSheetType(
    val mainSheetType: MainScreenType,
    val resId: Int
) {
    HOME(MainScreenType.HOME, TablerIcons.Home),
    SEARCH(MainScreenType.SEARCH, TablerIcons.Search),
    NOTIFICATION(MainScreenType.NOTIFICATION, TablerIcons.Bell)
}
