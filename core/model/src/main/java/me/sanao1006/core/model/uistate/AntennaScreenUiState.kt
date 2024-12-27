package me.sanao1006.core.model.uistate

import me.sanao1006.core.model.antenna.AntennaResponse

sealed interface AntennaScreenUiState {
    data object Loading : AntennaScreenUiState
    data object Failure : AntennaScreenUiState
    data class Success(
        val antennaList: List<AntennaResponse>
    ) : AntennaScreenUiState
}
