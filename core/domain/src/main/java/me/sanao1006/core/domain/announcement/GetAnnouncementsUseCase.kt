package me.sanao1006.core.domain.announcement

import me.sanao1006.core.data.repository.MetaRepository
import me.sanao1006.core.model.meta.Announcement
import me.sanao1006.core.model.requestbody.meta.MetaAnnouncementsRequestBody
import javax.inject.Inject

class GetAnnouncementsUseCaseImpl @Inject constructor(
    private val metaRepository: MetaRepository
) : GetAnnouncementsUseCase {
    override suspend operator fun invoke(
        limit: Int,
        sinceId: String,
        untilId: String,
        isActive: Boolean
    ) = metaRepository.getAnnouncements(
        body = MetaAnnouncementsRequestBody(
            limit = limit,
            sinceId = sinceId,
            untilId = untilId,
            isActive = isActive
        )
    )
}

interface GetAnnouncementsUseCase {
    suspend operator fun invoke(
        limit: Int = 10,
        sinceId: String = "",
        untilId: String = "",
        isActive: Boolean = true
    ): List<Announcement>
}
