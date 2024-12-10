package me.sanao1006.feature.search

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.slack.circuit.codegen.annotations.CircuitInject
import dagger.hilt.components.SingletonComponent
import ir.alirezaivaz.tablericons.TablerIcons
import me.sanao1006.core.ui.MainScreenBottomAppBar
import me.sanao1006.screens.MainScreenType
import me.sanao1006.screens.SearchScreen

@OptIn(ExperimentalMaterial3Api::class)
@CircuitInject(SearchScreen::class, SingletonComponent::class)
@Composable
fun SearchScreenUi(state: SearchScreen.State, modifier: Modifier) {
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(
                        onClick = {
                            state.eventSink(
                                SearchScreen.Event.OnNavigationIconClicked(
                                    drawerState,
                                    scope
                                )
                            )
                        }
                    ) { Icon(painter = painterResource(TablerIcons.Dots), "") }
                }
            )
        },
        bottomBar = {
            MainScreenBottomAppBar(
                mainSheetType = MainScreenType.SEARCH,
                onHomeClick = { },
                onSearchClick = { },
                onNotificationClick = { },
                floatingActionButton = { }
            )
        }
    ) {
        SearchScreenUiContent(modifier = modifier.padding(it))
    }
}

@Composable
private fun SearchScreenUiContent(modifier: Modifier = Modifier) {

}
