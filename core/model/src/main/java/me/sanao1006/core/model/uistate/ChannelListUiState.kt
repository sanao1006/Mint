package me.sanao1006.core.model.uistate

import me.sanao1006.core.model.notes.Channel

data class ChannelListUiState(
    val selectedTabIndex: Int = 0,
    val channelName: String = "",
    val channelList: List<Channel> = emptyList()
)
