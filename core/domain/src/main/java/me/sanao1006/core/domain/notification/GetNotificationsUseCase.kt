package me.sanao1006.core.domain.notification

import me.sanao1006.core.data.repository.AccountRepository
import me.sanao1006.core.model.requestbody.account.INotificationsRequestBody
import me.sanao1006.core.model.response.notes.NotesTimelineResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetNotificationsUseCase @Inject constructor(
    private val repository: AccountRepository
) {
    suspend operator fun invoke(): List<NotesTimelineResponse> {
        return repository.notifications(
            INotificationsRequestBody()
        )
    }
}
