package me.sanao1006.core.domain.antenna

import javax.inject.Inject
import me.sanao1006.core.data.repository.AntennaRepository
import me.sanao1006.core.model.requestbody.antenna.AntennaIdRequestBody

class DeleteAntennaUseCase @Inject constructor(
    private val antennaRepository: AntennaRepository
) {
    suspend operator fun invoke(antennaId: String) {
        antennaRepository.deleteAntenna(
            AntennaIdRequestBody(antennaId)
        )
    }
}
