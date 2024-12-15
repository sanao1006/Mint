package me.sanao1006.core.domain.notification

import javax.inject.Inject
import javax.inject.Singleton
import me.sanao1006.core.data.repository.AccountRepository
import me.sanao1006.core.model.requestbody.account.INotificationsRequestBody
import me.sanao1006.core.model.uistate.NotificationUiState

@Singleton
class GetNotificationsUseCase @Inject constructor(
    private val repository: AccountRepository
) {
    suspend operator fun invoke(): NotificationUiState {
        return try {
            val response = repository
                .notifications(INotificationsRequestBody())
                .map { it.toNotificationUiState() }
            NotificationUiState.Success(
                response
            )
        } catch (e: Exception) {
            NotificationUiState.Failure
        }
    }
}
