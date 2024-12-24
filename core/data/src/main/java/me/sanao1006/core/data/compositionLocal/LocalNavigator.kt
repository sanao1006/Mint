package me.sanao1006.core.data.compositionLocal

import androidx.compose.runtime.compositionLocalOf
import com.slack.circuit.runtime.Navigator

val LocalNavigator = compositionLocalOf<Navigator> {
    error("No navigator provided")
}
