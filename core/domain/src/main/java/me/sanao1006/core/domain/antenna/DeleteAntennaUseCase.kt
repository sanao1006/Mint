package me.sanao1006.core.domain.antenna

import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import me.sanao1006.core.data.repository.AntennaRepository
import me.sanao1006.core.data.util.suspendRunCatching
import me.sanao1006.core.model.requestbody.antenna.AntennaIdRequestBody
import me.sanao1006.core.network.di.IODispatcher

class DeleteAntennaUseCase @Inject constructor(
    private val antennaRepository: AntennaRepository,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(antennaId: String) = withContext(ioDispatcher) {
        suspendRunCatching {
            antennaRepository.deleteAntenna(
                AntennaIdRequestBody(antennaId)
            )
        }
    }
}
