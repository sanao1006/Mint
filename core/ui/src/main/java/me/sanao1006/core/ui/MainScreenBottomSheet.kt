package me.sanao1006.core.ui

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import ir.alirezaivaz.tablericons.TablerIcons

@Composable
fun MainScreenBottomSheet(modifier: Modifier = Modifier) {
    NavigationBar(
        modifier = modifier,
    ) {
        BottomSheetType.entries.forEach {
            NavigationBarItem(
                icon = { Icon(painter = painterResource(it.resId), contentDescription = null) },
                selected = false,
                onClick = it.onClick
            )
        }
    }
}

enum class BottomSheetType(val resId: Int, val onClick: () -> Unit) {
    HOME(TablerIcons.Home, { }),
    SEARCH(TablerIcons.Search, { }),
    NOTIFICATION(TablerIcons.Bell, { }),
}
