package me.sanao1006.screens

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen
import kotlinx.android.parcel.RawValue
import kotlinx.parcelize.Parcelize
import me.sanao1006.core.model.antenna.Antenna
import me.sanao1006.core.model.uistate.AntennaPostScreenUiState
import me.sanao1006.screens.AntennaSource.entries
import me.sanao1006.screens.event.globalIcon.GlobalIconEvent
import me.snao1006.res_value.ResString

@Parcelize
data class AntennaPostScreen(
    val antenna: @RawValue Antenna? = null
) : Screen {
    data class State(
        val uiState: AntennaPostScreenUiState,
        val screenType: AntennaPostScreenType,
        val globalIconEventSink: (GlobalIconEvent) -> Unit,
        val eventSink: (Event) -> Unit
    ) : CircuitUiState

    sealed class Event {
        data class OnAntennaNameChange(
            val name: String
        ) : Event()

        data class OnExpandClick(
            val isExpanded: Boolean
        ) : Event()

        data class OnDropDownItemClick(
            val antennaSource: String
        ) : Event()

        data class OnUsersNameChange(
            val users: String,
            val antennaSource: String
        ) : Event()

        data class OnBotAccountExcludedChange(
            val isBotAccountExcluded: Boolean
        ) : Event()

        data class OnReplyIncludedChange(
            val isReplyIncluded: Boolean
        ) : Event()

        data class OnKeywordValueChange(
            val keywordValue: String
        ) : Event()

        data class OnExceptedKeywordValueChange(
            val exceptedKeywordValue: String
        ) : Event()

        data class OnLocalOnlyChange(
            val isLocalOnly: Boolean
        ) : Event()

        data class OnCaseSensitiveChange(
            val isCaseSensitive: Boolean
        ) : Event()

        data class OnOnlyFileNoteChange(
            val isOnlyFileNote: Boolean
        ) : Event()

        data class OnSaveClick(
            val antenna: Antenna
        ) : Event()

        data class OnDeleteClick(
            val id: String
        ) : Event()
    }
}

enum class AntennaPostScreenType {
    CREATE,
    EDIT
}

enum class AntennaSource(@StringRes val resId: Int, val body: String) {
    ALL(ResString.antenna_source_all, "all"),
    HOME(ResString.antenna_source_home, "home"),
    USERS(ResString.antenna_source_user, "users"),

    //    LIST("list"),
    USERS_BLACKLIST(ResString.antenna_source_excepted_user, "users_blacklist");

    companion object {
        @Composable
        fun fromBodyToName(body: String): String {
            val resId = entries.find { it.body == body }?.resId ?: ALL.resId
            return stringResource(resId)
        }
    }
}
