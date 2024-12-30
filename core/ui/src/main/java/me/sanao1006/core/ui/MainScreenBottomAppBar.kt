package me.sanao1006.core.ui

import android.annotation.SuppressLint
import android.os.Vibrator
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingAppBarScrollBehavior
import androidx.compose.material3.HorizontalFloatingAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.getSystemService
import ir.alirezaivaz.tablericons.TablerIcons
import me.sanao1006.core.data.util.vibrate
import me.sanao1006.screens.MainScreenType
import me.sanao1006.screens.event.bottomAppBar.BottomAppBarActionEvent

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun MainScreenBottomAppBarWrapper(
    scrollBehavior: FloatingAppBarScrollBehavior?,
    mainScreenType: MainScreenType,
    modifier: Modifier = Modifier,
    event: (BottomAppBarActionEvent) -> Unit,
    onFabClick: () -> Unit
) = MainScreenBottomAppBar(
    scrollBehavior = scrollBehavior,
    mainSheetType = mainScreenType,
    onHomeClick = { event(BottomAppBarActionEvent.OnHomeIconClicked) },
    onNotificationClick = { event(BottomAppBarActionEvent.OnNotificationIconClicked) },
    modifier = modifier,
    onFabClick = onFabClick
)

@SuppressLint("MissingPermission")
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun MainScreenBottomAppBar(
    scrollBehavior: FloatingAppBarScrollBehavior?,
    mainSheetType: MainScreenType,
    onHomeClick: () -> Unit,
    onNotificationClick: () -> Unit,
    modifier: Modifier = Modifier,
    onFabClick: () -> Unit
) {
    val context = LocalContext.current
    val vibrator = context.getSystemService<Vibrator>()
    HorizontalFloatingAppBar(
        expanded = true,
        modifier = modifier,
        leadingContent = {
            IconButton(
                modifier = Modifier.padding(start = 8.dp, end = 16.dp),
                onClick = {
                    vibrator?.vibrate()
                    onHomeClick()
                }
            ) {
                Icon(
                    painter = painterResource(
                        if (mainSheetType == MainScreenType.HOME) {
                            TablerIcons.HomeFilled
                        } else {
                            TablerIcons.Home
                        }
                    ),
                    contentDescription = null
                )
            }
        },
        trailingContent = {
            IconButton(
                modifier = Modifier.padding(start = 16.dp, end = 8.dp),
                onClick = {
                    vibrator?.vibrate()
                    onNotificationClick()
                }
            ) {
                Icon(
                    painter = painterResource(
                        if (mainSheetType == MainScreenType.NOTIFICATION) {
                            TablerIcons.BellFilled
                        } else {
                            TablerIcons.Bell
                        }
                    ),
                    contentDescription = null
                )
            }
        },
        content = {
            FloatingActionButton(
                modifier = Modifier,
                containerColor = MaterialTheme.colorScheme.primary,
                onClick = onFabClick
            ) {
                Icon(painter = painterResource(TablerIcons.Pencil), "")
            }
        },
        scrollBehavior = scrollBehavior
    )
}

private enum class MainScreenType {
    HOME,
    NOTIFICATION
}
