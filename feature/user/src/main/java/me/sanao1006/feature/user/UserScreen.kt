package me.sanao1006.feature.user

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import coil3.compose.rememberAsyncImagePainter
import com.slack.circuit.codegen.annotations.CircuitInject
import dagger.hilt.components.SingletonComponent
import ir.alirezaivaz.tablericons.TablerIcons
import me.sanao1006.screens.UserScreen

@OptIn(ExperimentalMaterial3Api::class)
@CircuitInject(UserScreen::class, SingletonComponent::class)
@Composable
fun UserScreenUi(state: UserScreen.State, modifier: Modifier) {
    Box(modifier = modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { },
                    navigationIcon = {
                        IconButton(
                            onClick = { state.eventSink(UserScreen.Event.OnNavigationIconClicked) }
                        ) {
                            Icon(painter = painterResource(TablerIcons.ArrowLeft), "")
                        }
                    }
                )
            }
        ) {
            Column(modifier = Modifier.padding(it)) {
                Image(painter = rememberAsyncImagePainter(state.uiState.avatarUrl), "")
            }
        }
    }
}
