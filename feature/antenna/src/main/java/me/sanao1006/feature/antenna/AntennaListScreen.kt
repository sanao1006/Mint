package me.sanao1006.feature.antenna

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.slack.circuit.codegen.annotations.CircuitInject
import dagger.hilt.components.SingletonComponent
import me.sanao1006.screens.AntennaListScreen

@CircuitInject(AntennaListScreen::class, SingletonComponent::class)
@Composable
fun AntennaListScreen(state: AntennaListScreen.State, modifier: Modifier) {
    Box(modifier = modifier.fillMaxSize()) {}
}
