package me.sanao1006.core.ui

import android.annotation.SuppressLint
import android.os.Vibrator
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarScrollBehavior
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreenBottomAppBarWrapper(
    scrollBehavior: BottomAppBarScrollBehavior,
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

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("MissingPermission")
@Composable
private fun MainScreenBottomAppBar(
    scrollBehavior: BottomAppBarScrollBehavior,
    mainSheetType: MainScreenType,
    onHomeClick: () -> Unit,
    onNotificationClick: () -> Unit,
    modifier: Modifier = Modifier,
    onFabClick: () -> Unit
) {
    val context = LocalContext.current
    val vibrator = context.getSystemService<Vibrator>()
    BottomAppBar(
        scrollBehavior = scrollBehavior,
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.background,
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier,
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                onClick = onFabClick
            ) {
                Icon(painter = painterResource(TablerIcons.Pencil), "")
            }
        },
        actions = {
            MainScreenType.entries.forEach {
                IconButton(
                    modifier = Modifier.padding(start = 16.dp, end = 8.dp),
                    onClick = {
                        vibrator?.vibrate()
                        when (it) {
                            MainScreenType.HOME -> onHomeClick()
                            MainScreenType.NOTIFICATION -> onNotificationClick()
                        }
                    }
                ) {
                    Icon(
                        painter = painterResource(
                            when (it) {
                                MainScreenType.HOME -> {
                                    if (mainSheetType == MainScreenType.HOME) {
                                        TablerIcons.HomeFilled
                                    } else {
                                        TablerIcons.Home
                                    }
                                }

                                MainScreenType.NOTIFICATION -> {
                                    if (mainSheetType == MainScreenType.NOTIFICATION) {
                                        TablerIcons.BellFilled
                                    } else {
                                        TablerIcons.Bell
                                    }
                                }
                            }
                        ),
                        contentDescription = null
                    )
                }
            }
        }
    )
}
