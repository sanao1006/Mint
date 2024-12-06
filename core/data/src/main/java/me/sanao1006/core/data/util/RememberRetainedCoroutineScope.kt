package me.sanao1006.core.data.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.RememberObserver
import androidx.compose.runtime.Stable
import com.slack.circuit.retained.rememberRetained
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel

@Stable
class StableCoroutineScope(scope: CoroutineScope) : CoroutineScope by scope

@Composable
fun rememberRetainedCoroutineScope(): StableCoroutineScope {
  return rememberRetained("coroutine_scope") {
    object : RememberObserver {
      val scope = StableCoroutineScope(CoroutineScope(Dispatchers.Main + Job()))

      override fun onAbandoned() = onForgotten()

      override fun onForgotten() {
        scope.cancel()
      }

      override fun onRemembered() = Unit
    }
  }.scope
}
