package me.sanao1006.screens.event

import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.GoToNavigator
import me.sanao1006.screens.NoteScreen
import me.sanao1006.screens.ReplyObject
import me.sanao1006.screens.UserScreen

sealed class TimelineItemEvent : CircuitUiEvent {
    data class OnTimelineItemIconClicked(
        val id: String,
        val username: String?,
        val host: String?
    ) : TimelineItemEvent()

    data class OnTimelineItemReplyClicked(
        val id: String,
        val user: String,
        val host: String?
    ) : TimelineItemEvent()

    data class OnTimelineItemRepostClicked(val id: String) : TimelineItemEvent()
    data class OnTimelineItemReactionClicked(val id: String) : TimelineItemEvent()
    data class OnTimelineItemOptionClicked(
        val id: String,
        val userId: String?,
        val host: String?,
        val username: String?,
        val text: String,
        val uri: String
    ) : TimelineItemEvent()

    data class OnRenoteClicked(
        val id: String
    ) : TimelineItemEvent()

    data class OnQuoteClicked(
        val id: String
    ) : TimelineItemEvent()

    data class OnDetailClicked(
        val id: String,
        val username: String?,
        val host: String?
    ) : TimelineItemEvent()

    data class OnCopyClicked(
        val text: String
    ) : TimelineItemEvent()

    data class OnCopyLinkClicked(
        val link: String
    ) : TimelineItemEvent()

    data class OnShareClicked(
        val link: String
    ) : TimelineItemEvent()

    data class OnFavoriteClicked(
        val noteId: String
    ) : TimelineItemEvent()
}

fun TimelineItemEvent.OnTimelineItemIconClicked.handleTimelineItemIconClicked(
    navigator: GoToNavigator
) {
    navigator.goTo(
        UserScreen(
            userId = this.id,
            userName = this.username,
            host = this.host
        )
    )
}

fun TimelineItemEvent.OnTimelineItemReplyClicked.handleTimelineItemReplyClicked(
    navigator: GoToNavigator
) {
    val user = buildString {
        append("@${this@handleTimelineItemReplyClicked.user}")
        if (!this@handleTimelineItemReplyClicked.host.isNullOrEmpty()) {
            append("@${this@handleTimelineItemReplyClicked.host}")
        }
    }
    navigator.goTo(
        NoteScreen(
            replyObject = ReplyObject(
                id = this.id,
                user = user
            )
        )
    )
}
