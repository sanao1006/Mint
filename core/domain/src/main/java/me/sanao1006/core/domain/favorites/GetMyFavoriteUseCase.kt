package me.sanao1006.core.domain.favorites

import javax.inject.Inject
import me.sanao1006.core.data.repository.AccountRepository
import me.sanao1006.core.model.requestbody.account.IFavoritesRequestBody
import me.sanao1006.core.model.response.notes.NotesTimelineResponse

class GetMyFavoriteUseCase @Inject constructor(
    private val accountRepository: AccountRepository
) {
    suspend operator fun invoke(
        limit: Int = 10,
        sinceId: String? = null,
        untilId: String? = null
    ): List<NotesTimelineResponse> {
        return accountRepository.getMyFavorites(
            body = IFavoritesRequestBody(
                limit = limit,
                sinceId = sinceId,
                untilId = untilId
            )
        ).map { it.note }
    }
}
