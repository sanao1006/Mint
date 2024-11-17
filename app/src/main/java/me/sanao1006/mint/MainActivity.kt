package me.sanao1006.mint

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.slack.circuit.backstack.rememberSaveableBackStack
import com.slack.circuit.foundation.Circuit
import com.slack.circuit.foundation.CircuitCompositionLocals
import com.slack.circuit.foundation.NavigableCircuitContent
import com.slack.circuit.foundation.rememberCircuitNavigator
import com.slack.circuit.overlay.ContentWithOverlays
import com.slack.circuitx.android.rememberAndroidScreenAwareNavigator
import dagger.hilt.android.AndroidEntryPoint
import me.sanao1006.feature.home.HomeScreen
import me.sanao1006.feature.login.LoginScreen
import me.sanao1006.mint.ui.theme.MintTheme
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var circuit: Circuit

    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MintTheme {
                val uiState = viewModel.uiState.collectAsState()
                when (uiState.value.tokenState) {
                    TokenState.LOADING -> {
                        /* Display nothing while loading. */
                    }

                    TokenState.SUCCESS -> {
                        val backstack = rememberSaveableBackStack(
                            if (uiState.value.isLoggedIn) HomeScreen
                            else LoginScreen
                        )
                        val circuitNavigator = rememberCircuitNavigator(backstack)
                        val navigator = rememberAndroidScreenAwareNavigator(circuitNavigator, this)

                        CompositionLocalProvider(
                            LocalContext provides this,
                        ) {
                            CircuitCompositionLocals(circuit) {
                                ContentWithOverlays {
                                    NavigableCircuitContent(
                                        navigator = navigator,
                                        backStack = backstack,
                                        circuit = circuit
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MintTheme {
        Greeting("Android")
    }
}