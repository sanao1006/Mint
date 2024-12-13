package me.sanao1006.feature.home

import me.sanao1006.core.domain.home.TimelineType
import me.sanao1006.screens.HomeScreen

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
