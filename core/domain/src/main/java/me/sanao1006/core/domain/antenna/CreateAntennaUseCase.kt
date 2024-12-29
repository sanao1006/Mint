package me.sanao1006.core.domain.antenna

import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import me.sanao1006.core.data.repository.AntennaRepository
import me.sanao1006.core.data.util.suspendRunCatching
import me.sanao1006.core.model.requestbody.antenna.AntennaCreateRequestBody
import me.sanao1006.core.network.di.IODispatcher

class CreateAntennaUseCase @Inject constructor(
    private val antennaRepository: AntennaRepository,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(
        name: String,
        src: String,
        userListId: String? = null,
        keywords: List<List<String>> = emptyList(),
        excludeKeywords: List<List<String>> = emptyList(),
        users: List<String> = listOf(""),
        caseSensitive: Boolean = false,
        localOnly: Boolean = false,
        excludeBots: Boolean = false,
        withReplies: Boolean = false,
        withFile: Boolean = false
    ) = withContext(ioDispatcher) {
        suspendRunCatching {
            antennaRepository.createAntenna(
                body = AntennaCreateRequestBody(
                    name = name,
                    src = src,
                    userListId = userListId,
                    keywords = keywords,
                    excludeKeywords = excludeKeywords,
                    users = users,
                    caseSensitive = caseSensitive,
                    localOnly = localOnly,
                    excludeBots = excludeBots,
                    withReplies = withReplies,
                    withFile = withFile,
                    notify = false
                )
            )
        }
    }
}
