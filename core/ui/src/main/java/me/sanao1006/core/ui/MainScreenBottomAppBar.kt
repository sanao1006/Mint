package me.sanao1006.core.ui

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import ir.alirezaivaz.tablericons.TablerIcons
import me.sanao1006.screens.MainScreenType

@Composable
fun MainScreenBottomAppBar(
    mainSheetType: MainScreenType,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier,
    ) {
        BottomSheetType.entries.forEach {
            NavigationBarItem(
                icon = { Icon(painter = painterResource(it.resId), contentDescription = null) },
                selected = it.mainSheetType == mainSheetType,
                onClick = it.onClick
            )
        }
    }
}

private enum class BottomSheetType(
    val mainSheetType: MainScreenType,
    val resId: Int,
    val onClick: () -> Unit
) {
    HOME(MainScreenType.HOME, TablerIcons.Home, { }),
    SEARCH(MainScreenType.SEARCH, TablerIcons.Search, { }),
    NOTIFICATION(MainScreenType.NOTIFICATION, TablerIcons.Bell, { }),
    INFO(MainScreenType.Info, TablerIcons.LayoutGridAdd, { })
}
