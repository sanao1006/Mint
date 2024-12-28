package me.sanao1006.core.domain.antenna

import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import me.sanao1006.core.data.repository.AntennaRepository
import me.sanao1006.core.model.requestbody.antenna.AntennasNotesRequestBody
import me.sanao1006.core.model.uistate.AntennaListUiState
import me.sanao1006.core.network.di.IODispatcher

class GetAntennasNotesUseCase @Inject constructor(
    private val antennaRepository: AntennaRepository,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(
        antennaId: String,
        limit: Int = 10,
        sinceId: String? = null,
        untilId: String? = null,
        sinceDate: String? = null,
        untilDate: String? = null
    ): AntennaListUiState {
        return withContext(ioDispatcher) {
            try {
                val response = antennaRepository.getAntennaNotes(
                    body = AntennasNotesRequestBody(
                        antennaId = antennaId,
                        limit = limit,
                        sinceId = sinceId,
                        untilId = untilId,
                        sinceDate = sinceDate,
                        untilDate = untilDate
                    )
                )
                AntennaListUiState(
                    timelineItems = response.map { it.toTimelineUiState() }
                )
            } catch (e: Exception) {
                AntennaListUiState(
                    timelineItems = emptyList()
                )
            }
        }
    }
}
