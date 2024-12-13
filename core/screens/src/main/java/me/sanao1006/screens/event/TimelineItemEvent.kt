package me.sanao1006.screens.event

import com.slack.circuit.runtime.GoToNavigator
import me.sanao1006.screens.UserScreen

sealed class TimelineItemEvent {
    data class OnTimelineItemIconClicked(
        val id: String,
        val username: String?,
        val host: String?
    ) : TimelineItemEvent()

    data class OnTimelineItemReplyClicked(val id: String) : TimelineItemEvent()
    data class OnTimelineItemRepostClicked(val id: String) : TimelineItemEvent()
    data class OnTimelineItemReactionClicked(val id: String) : TimelineItemEvent()
    data class OnTimelineItemOptionClicked(val id: String) : TimelineItemEvent()
}

fun TimelineItemEvent.handleTimelineItemEvent(
    navigator: GoToNavigator
) {
    when (this) {
        is TimelineItemEvent.OnTimelineItemIconClicked -> {
            navigator.goTo(
                UserScreen(
                    userId = this.id,
                    userName = this.username,
                    host = this.host
                )
            )
        }

        is TimelineItemEvent.OnTimelineItemReplyClicked -> {}
        is TimelineItemEvent.OnTimelineItemRepostClicked -> {}
        is TimelineItemEvent.OnTimelineItemReactionClicked -> {}
        is TimelineItemEvent.OnTimelineItemOptionClicked -> {}
    }
}
