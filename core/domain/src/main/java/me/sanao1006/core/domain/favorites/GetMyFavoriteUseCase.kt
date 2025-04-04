package me.sanao1006.core.domain.favorites

import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import me.sanao1006.core.data.repository.AccountRepository
import me.sanao1006.core.model.requestbody.account.IFavoritesRequestBody
import me.sanao1006.core.model.uistate.FavoritesScreenUiState
import me.sanao1006.core.network.di.IODispatcher
import timber.log.Timber

class GetMyFavoriteUseCase @Inject constructor(
    private val accountRepository: AccountRepository,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(
        limit: Int = 10,
        sinceId: String? = null,
        untilId: String? = null
    ): FavoritesScreenUiState {
        return withContext(ioDispatcher) {
            try {
                val response = accountRepository.getMyFavorites(
                    IFavoritesRequestBody(
                        limit = limit,
                        sinceId = sinceId,
                        untilId = untilId
                    )
                )
                FavoritesScreenUiState(
                    timelineItems = response.map { it.note.toTimelineUiState() }
                )
            } catch (e: Exception) {
                Timber.e(e)
                FavoritesScreenUiState(
                    timelineItems = emptyList()
                )
            }
        }
    }
}
