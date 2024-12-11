package me.sanao1006.feature.search

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.slack.circuit.codegen.annotations.CircuitInject
import dagger.hilt.components.SingletonComponent
import ir.alirezaivaz.tablericons.TablerIcons
import me.sanao1006.core.ui.MainScreenBottomAppBarWrapper
import me.sanao1006.core.ui.MainScreenDrawerWrapper
import me.sanao1006.screens.MainScreenType
import me.sanao1006.screens.SearchScreen
import me.sanao1006.screens.event.GlobalIconEvent

@OptIn(ExperimentalMaterial3Api::class)
@CircuitInject(SearchScreen::class, SingletonComponent::class)
@Composable
fun SearchScreenUi(state: SearchScreen.State, modifier: Modifier) {
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    Box(modifier = modifier.fillMaxSize()) {
        MainScreenDrawerWrapper(
            loginUserInfo = state.loginUserInfo,
            drawerState = drawerState,
            event = state.drawerEventSink
        ) {
            SearchScreenUiContent(
                state = state,
                onGlobalClick = {
                    state.globalIconEventSink(
                        GlobalIconEvent.OnGlobalIconClicked(drawerState, scope)
                    )
                },
                modifier = modifier
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchScreenUiContent(
    state: SearchScreen.State,
    onGlobalClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = onGlobalClick) {
                        Icon(
                            painter = painterResource(TablerIcons.Menu2),
                            contentDescription = null
                        )
                    }
                }
            )
        },
        bottomBar = {
            MainScreenBottomAppBarWrapper(
                mainScreenType = MainScreenType.SEARCH,
                modifier = Modifier,
                event = state.bottomAppBarActionEventSink
            )
        }
    ) {
        Column(modifier = Modifier.padding(it)) {
            Text(text = "Search Screen")
        }
    }
}
