package me.sanao1006.core.data.util

import timber.log.Timber
import kotlin.coroutines.cancellation.CancellationException

// ref: https://github.com/android/nowinandroid/blob/c0ba97e1705d842700fd0da6628f8188e9cbc1e0/core/data/src/main/java/com/google/samples/apps/nowinandroid/core/data/SyncUtilities.kt#L53-L68
private suspend fun <T> suspendRunCatching(block: suspend () -> T): Result<T> = try {
    Result.success(block())
} catch (cancellationException: CancellationException) {
    throw cancellationException
} catch (exception: Exception) {
    Timber
        .tag("suspendRunCatching")
        .i("Failed to evaluate a suspendRunCatchingBlock. Returning failure Result $exception")
    Result.failure(exception)
}
