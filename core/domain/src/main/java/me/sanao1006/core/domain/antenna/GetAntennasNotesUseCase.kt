package me.sanao1006.core.domain.antenna

import javax.inject.Inject
import me.sanao1006.core.data.repository.AntennaRepository
import me.sanao1006.core.model.requestbody.antenna.AntennasNotesRequestBody
import me.sanao1006.core.model.uistate.AntennaListUiState

class GetAntennasNotesUseCase @Inject constructor(
    private val antennaRepository: AntennaRepository
) {
    suspend operator fun invoke(
        antennaId: String,
        limit: Int = 10,
        sinceId: String? = null,
        untilId: String? = null,
        sinceDate: String? = null,
        untilDate: String? = null
    ): AntennaListUiState {
        return try {
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
