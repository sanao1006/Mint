package me.sanao1006.feature.home

import android.content.Context
import com.slack.circuit.runtime.GoToNavigator
import kotlinx.coroutines.launch
import me.sanao1006.core.domain.home.TimelineType
import me.sanao1006.screens.HomeScreen
import me.sanao1006.screens.NoteScreen
import me.sanao1006.screens.UserScreen
import me.snao1006.res_value.ResString

internal fun handleNoteCreated(
    event: HomeScreen.Event.OnNoteCreated,
    isSuccessCreateNote: Boolean?,
    context: Context
) {
    isSuccessCreateNote?.let { flg ->
        event.scope.launch {
            event.snackbarHostState.showSnackbar(
                message = if (flg) {
                    context.getString(ResString.post_result_message_success)
                } else {
                    context.getString(ResString.post_result_message_failed)
                }
            )
        }
    }
}

internal fun handleNoteCreateClicked(nav: GoToNavigator) {
    nav.goTo(NoteScreen)
}

internal fun handleTimelineEvent(
    event: HomeScreen.Event.TimelineEvent,
    setTimelineType: (TimelineType) -> Unit
) {
    when (event) {
        HomeScreen.Event.TimelineEvent.OnLocalTimelineClicked -> setTimelineType(TimelineType.LOCAL)
        HomeScreen.Event.TimelineEvent.OnSocialTimelineClicked -> setTimelineType(
            TimelineType.SOCIAL
        )

        HomeScreen.Event.TimelineEvent.OnGlobalTimelineClicked -> setTimelineType(
            TimelineType.GLOBAL
        )
    }
}

internal fun handleTimelineItemEvent(
    event: HomeScreen.Event.TimelineItemEvent,
    nav: GoToNavigator
) {
    when (event) {
        is HomeScreen.Event.TimelineItemEvent.OnTimelineIconClicked -> {
            nav.goTo(
                UserScreen(
                    userId = event.userId,
                    userName = event.userName,
                    host = event.host
                )
            )
        }
    }
}
