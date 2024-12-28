package me.sanao1006.core.domain.antenna

import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import me.sanao1006.core.data.repository.AntennaRepository
import me.sanao1006.core.data.util.suspendRunCatching
import me.sanao1006.core.model.requestbody.antenna.AntennaUpdateRequestBody
import me.sanao1006.core.network.di.IODispatcher

class UpdateAntennaUseCase @Inject constructor(
    private val antennaRepository: AntennaRepository,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(
        antennaId: String,
        name: String,
        src: String,
        userListId: String? = null,
        keywords: List<List<String>> = emptyList(),
        excludeKeywords: List<List<String>> = emptyList(),
        users: List<String> = emptyList(),
        canSensitive: Boolean,
        localOnly: Boolean? = null,
        excludeBots: Boolean? = null,
        withReplies: Boolean,
        withFile: Boolean
    ) = withContext(ioDispatcher) {
        suspendRunCatching {
            antennaRepository.updateAntenna(
                body = AntennaUpdateRequestBody(
                    antennaId = antennaId,
                    name = name,
                    src = src,
                    userListId = userListId,
                    keywords = keywords,
                    excludeKeywords = excludeKeywords,
                    users = users,
                    canSensitive = canSensitive,
                    localOnly = localOnly,
                    excludeBots = excludeBots,
                    withReplies = withReplies,
                    withFile = withFile
                )
            )
        }
    }
}
