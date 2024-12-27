package me.sanao1006.core.domain.antenna

import javax.inject.Inject
import me.sanao1006.core.data.repository.AntennaRepository
import me.sanao1006.core.model.uistate.AntennaScreenUiState
import timber.log.Timber

class GetAntennasUseCase @Inject constructor(
    private val antennaRepository: AntennaRepository
) {
    suspend operator fun invoke(): AntennaScreenUiState {
        return try {
            val response = antennaRepository.getAntennaList()
            AntennaScreenUiState.Success(response)
        } catch (e: Exception) {
            Timber.e(e)
            AntennaScreenUiState.Failure
        }
    }
}
