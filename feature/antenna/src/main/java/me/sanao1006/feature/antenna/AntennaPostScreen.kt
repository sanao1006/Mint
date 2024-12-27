package me.sanao1006.feature.antenna

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.slack.circuit.codegen.annotations.CircuitInject
import dagger.hilt.components.SingletonComponent
import me.sanao1006.screens.event.AntennaPostScreen


@CircuitInject(AntennaPostScreen::class, SingletonComponent::class)
@Composable
fun AntennaPostScreen(state: AntennaPostScreen.State, modifier: Modifier) {
    Box(modifier = modifier.fillMaxSize()) {}
}
