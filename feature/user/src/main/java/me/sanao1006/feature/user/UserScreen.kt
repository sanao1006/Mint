package me.sanao1006.feature.user

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.slack.circuit.codegen.annotations.CircuitInject
import dagger.hilt.components.SingletonComponent
import me.sanao1006.screens.UserScreen

@CircuitInject(UserScreen::class, SingletonComponent::class)
@Composable
fun UserScreenUi(state: UserScreen.State, modifier: Modifier) {
    Box(modifier = modifier.fillMaxSize()) {
        Scaffold {
            Column(modifier = Modifier.padding(it)) {
                Text("hello")
            }
        }
    }
}
