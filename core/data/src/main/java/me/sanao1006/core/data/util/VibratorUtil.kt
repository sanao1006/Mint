package me.sanao1006.core.data.util

import android.annotation.SuppressLint
import android.os.VibrationEffect
import android.os.Vibrator

@SuppressLint("MissingPermission")
fun Vibrator.vibrate() {
    cancel()
    vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK))
}
