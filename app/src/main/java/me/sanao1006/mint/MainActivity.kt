package me.sanao1006.mint

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.slack.circuit.backstack.rememberSaveableBackStack
import com.slack.circuit.foundation.Circuit
import com.slack.circuit.foundation.CircuitCompositionLocals
import com.slack.circuit.foundation.NavigableCircuitContent
import com.slack.circuit.foundation.rememberCircuitNavigator
import com.slack.circuit.overlay.ContentWithOverlays
import com.slack.circuitx.android.rememberAndroidScreenAwareNavigator
import com.slack.circuitx.gesturenavigation.GestureNavigationDecoration
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import me.sanao1006.core.data.compositionLocal.LocalNavigator
import me.sanao1006.core.designsystem.MintTheme
import me.sanao1006.screens.HomeScreen
import me.sanao1006.screens.LoginScreen

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var circuit: Circuit

    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashscreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MintTheme {
                val uiState = viewModel.uiState.collectAsState()
                splashscreen.setKeepOnScreenCondition { !uiState.value.tokenLoadingState }

                if (uiState.value.tokenLoadingState) {
                    val backstack = rememberSaveableBackStack(
                        if (uiState.value.isLoggedIn) HomeScreen
                        else LoginScreen
                    )
                    val circuitNavigator = rememberCircuitNavigator(backstack)
                    val navigator = rememberAndroidScreenAwareNavigator(circuitNavigator, this)

                    CompositionLocalProvider(
                        LocalContext provides this,
                        LocalNavigator provides navigator
                    ) {
                        CircuitCompositionLocals(circuit) {
                            ContentWithOverlays {
                                Surface {
                                    NavigableCircuitContent(
                                        navigator = navigator,
                                        backStack = backstack,
                                        circuit = circuit,
                                        decoration = GestureNavigationDecoration {
                                            navigator.pop()
                                        }
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
