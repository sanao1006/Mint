package me.sanao1006.feature.announcement

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ContainedLoadingIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.slack.circuit.codegen.annotations.CircuitInject
import dagger.hilt.components.SingletonComponent
import me.sanao1006.core.model.uistate.AnnouncementUiState
import me.sanao1006.core.ui.DrawerItem
import me.sanao1006.core.ui.DrawerItemScreenWrapper
import me.sanao1006.screens.AnnouncementScreen
import me.snao1006.res_value.ResString

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
@CircuitInject(AnnouncementScreen::class, SingletonComponent::class)
fun AnnouncementScreen(state: AnnouncementScreen.State, modifier: Modifier) {
    Box(modifier = modifier.fillMaxSize()) {
        DrawerItemScreenWrapper(
            drawerItem = DrawerItem.ANNOUNCEMENT,
            globalIconEventSink = state.globalIconEventSink
        ) {
            Column(modifier = it.fillMaxSize()) {
                when (state.uiState) {
                    is AnnouncementUiState.Loading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            ContainedLoadingIndicator()
                        }
                    }

                    is AnnouncementUiState.Failed -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(stringResource(ResString.no_contents))
                        }
                    }

                    is AnnouncementUiState.Success -> {
                        val announcements =
                            (state.uiState as AnnouncementUiState.Success).announcements
                        announcements.forEach {
                            Text(text = it.title)
                        }
                    }
                }
            }
        }
    }
}
