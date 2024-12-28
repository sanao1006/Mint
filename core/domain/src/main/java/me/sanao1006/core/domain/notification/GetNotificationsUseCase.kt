package me.sanao1006.core.domain.notification

import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import me.sanao1006.core.data.repository.AccountRepository
import me.sanao1006.core.model.requestbody.account.INotificationsRequestBody
import me.sanao1006.core.model.uistate.NotificationUiState
import me.sanao1006.core.network.di.IODispatcher
import timber.log.Timber

@Singleton
class GetNotificationsUseCase @Inject constructor(
    private val repository: AccountRepository,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(): NotificationUiState {
        return withContext(ioDispatcher) {
            try {
                val response = repository.notifications(INotificationsRequestBody())
                NotificationUiState(
                    notificationUiStateObjects = response.map { it.toNotificationUiState() }
                )
            } catch (e: Exception) {
                Timber.e(e)
                NotificationUiState()
            }
        }
    }
}
