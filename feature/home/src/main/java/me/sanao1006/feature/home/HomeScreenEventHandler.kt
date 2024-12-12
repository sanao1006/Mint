package me.sanao1006.feature.home

import com.slack.circuit.runtime.GoToNavigator
import me.sanao1006.core.domain.home.TimelineType
import me.sanao1006.screens.HomeScreen
import me.sanao1006.screens.UserScreen

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
