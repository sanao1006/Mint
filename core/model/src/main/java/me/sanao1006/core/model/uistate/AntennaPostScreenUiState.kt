package me.sanao1006.core.model.uistate

data class AntennaPostScreenUiState(
    val antennaName: String = "",
    val expanded: Boolean = false,
    val antennaSource: String = "",
    val users: List<String>? = null,
    val isBotAccountExcluded: Boolean = false,
    val isReplyIncluded: Boolean = false,
    val keywordValue: String = "",
    val exceptedKeywordValue: String = "",
    val isLocalOnly: Boolean = false,
    val isCaseSensitive: Boolean = false,
    val isOnlyFileNote: Boolean = false
)
