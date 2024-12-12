package me.sanao1006.feature.search

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.slack.circuit.codegen.annotations.CircuitInject
import dagger.hilt.components.SingletonComponent
import ir.alirezaivaz.tablericons.TablerIcons
import me.sanao1006.screens.SearchScreen
import me.sanao1006.screens.event.GlobalIconEvent
import me.snao1006.res_value.ResString

@CircuitInject(SearchScreen::class, SingletonComponent::class)
@Composable
fun SearchScreenUi(state: SearchScreen.State, modifier: Modifier) {
    Box(modifier = modifier.fillMaxSize()) {
        SearchScreenUiContent(
            state = state,
            onGlobalClick = {
                state.globalIconEventSink(GlobalIconEvent.OnArrowBackIconClicked)
            },
            modifier = modifier
        )
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
                title = { Text(stringResource(ResString.drawer_item_search)) },
                navigationIcon = {
                    IconButton(onClick = onGlobalClick) {
                        Icon(
                            painter = painterResource(TablerIcons.ArrowLeft),
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) {
        Column(modifier = Modifier.padding(it)) {
            Text(text = "Search Screen")
        }
    }
}
