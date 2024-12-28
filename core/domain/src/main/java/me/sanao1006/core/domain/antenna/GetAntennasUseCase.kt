package me.sanao1006.core.domain.antenna

import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import me.sanao1006.core.data.repository.AntennaRepository
import me.sanao1006.core.model.uistate.AntennaScreenUiState
import me.sanao1006.core.network.di.IODispatcher
import timber.log.Timber

class GetAntennasUseCase @Inject constructor(
    private val antennaRepository: AntennaRepository,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(): AntennaScreenUiState {
        return withContext(ioDispatcher) {
            try {
                val response = antennaRepository.getAntennaList()
                AntennaScreenUiState.Success(response)
            } catch (e: Exception) {
                Timber.e(e)
                AntennaScreenUiState.Failure
            }
        }
    }
}
