package me.sanao1006.feature.favorites

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.slack.circuit.codegen.annotations.CircuitInject
import dagger.hilt.components.SingletonComponent
import me.sanao1006.screens.FavoritesScreen

@CircuitInject(FavoritesScreen::class, SingletonComponent::class)
@Composable
fun FavoritesScreen(state: FavoritesScreen.State, modifier: Modifier) {
}
