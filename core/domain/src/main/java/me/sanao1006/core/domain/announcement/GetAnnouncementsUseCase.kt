package me.sanao1006.core.domain.announcement

import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import me.sanao1006.core.data.repository.MetaRepository
import me.sanao1006.core.model.requestbody.meta.MetaAnnouncementsRequestBody
import me.sanao1006.core.model.uistate.AnnouncementUiState
import me.sanao1006.core.network.di.IODispatcher
import timber.log.Timber

class GetAnnouncementsUseCaseImpl @Inject constructor(
    private val metaRepository: MetaRepository,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher
) : GetAnnouncementsUseCase {
    override suspend operator fun invoke(
        limit: Int,
        sinceId: String?,
        untilId: String?,
        isActive: Boolean
    ): AnnouncementUiState {
        return withContext(ioDispatcher) {
            try {
                val announcements = metaRepository.getAnnouncements(
                    body = MetaAnnouncementsRequestBody(
                        limit = limit,
                        sinceId = sinceId,
                        untilId = untilId,
                        isActive = isActive
                    )
                )
                AnnouncementUiState.Success(announcements)
            } catch (e: Exception) {
                Timber.e(e)
                AnnouncementUiState.Failed
            }
        }
    }
}

interface GetAnnouncementsUseCase {
    suspend operator fun invoke(
        limit: Int = 10,
        sinceId: String? = null,
        untilId: String? = null,
        isActive: Boolean = true
    ): AnnouncementUiState
}
