package me.sanao1006.screens.event

import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen
import kotlinx.android.parcel.RawValue
import kotlinx.parcelize.Parcelize
import me.sanao1006.core.model.antenna.Antenna

@Parcelize
data class AntennaPostScreen(
    val antenna: @RawValue Antenna? = null
) : Screen {
    data class State(
        val screenType: AntennaPostScreenType,
        val eventSink: (Event) -> Unit
    ) : CircuitUiState

    sealed class Event {
        data class OnSaveClick(
            val antenna: Antenna
        ) : Event()

        data class OnDeleteClick(
            val id: String
        ) : Event()
    }
}

enum class AntennaPostScreenType {
    CREATE,
    EDIT
}
