package me.sanao1006.core.data.compositionLocal

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.compositionLocalOf

val LocalLazyListStateProvider = compositionLocalOf { LazyListState() }
