package me.sanao1006.mint

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.slack.circuit.backstack.rememberSaveableBackStack
import com.slack.circuit.foundation.Circuit
import com.slack.circuit.foundation.CircuitCompositionLocals
import com.slack.circuit.foundation.NavigableCircuitContent
import com.slack.circuit.foundation.rememberCircuitNavigator
import com.slack.circuit.overlay.ContentWithOverlays
import com.slack.circuit.runtime.screen.Screen
import com.slack.circuitx.android.rememberAndroidScreenAwareNavigator
import com.slack.circuitx.gesturenavigation.GestureNavigationDecoration
import dagger.hilt.android.AndroidEntryPoint
import me.sanao1006.core.data.compositionLocal.LocalNavigator
import me.sanao1006.core.designsystem.MintTheme
import me.sanao1006.screens.HomeScreen
import me.sanao1006.screens.LoginScreen
import javax.inject.Inject

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
                window.navigationBarColor = MaterialTheme.colorScheme.background.toArgb()
                val uiState = viewModel.uiState.collectAsStateWithLifecycle()
                splashscreen.setKeepOnScreenCondition { !uiState.value.tokenLoadingState }

                if (uiState.value.tokenLoadingState) {
                    if (uiState.value.isLoggedIn) {
                        SetupNavigator(circuit, HomeScreen, this)
                    } else {
                        SetupNavigator(circuit, LoginScreen, this)
                    }
                }
            }
        }
    }
}

@Composable
private fun SetupNavigator(
    circuit: Circuit,
    screen: Screen,
    context: Context
) {
    val backstack = rememberSaveableBackStack(screen)
    val circuitNavigator = rememberCircuitNavigator(backstack)
    val navigator = rememberAndroidScreenAwareNavigator(circuitNavigator, context)

    CompositionLocalProvider(
        LocalContext provides context,
        LocalNavigator provides navigator
    ) {
        CircuitCompositionLocals(circuit) {
            ContentWithOverlays {
                Surface {
                    NavigableCircuitContent(
                        navigator = navigator,
                        backStack = backstack,
                        circuit = circuit,
                        decoration = GestureNavigationDecoration(onBackInvoked = navigator::pop)
                    )
                }
            }
        }
    }
}
