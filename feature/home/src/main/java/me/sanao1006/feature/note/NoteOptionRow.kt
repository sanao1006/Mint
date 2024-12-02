package me.sanao1006.feature.note

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.slack.circuit.overlay.OverlayEffect
import com.slack.circuitx.overlays.BottomSheetOverlay
import ir.alirezaivaz.tablericons.TablerIcons

// 共通インターフェースの定義
interface ModelSheetItem {
    val resId: Int
    val description: String
}

// 各enumクラスでインターフェースを実装
enum class VisibilityItem(
    val value: me.sanao1006.core.model.notes.Visibility,
    override val resId: Int,
    override val description: String
) : ModelSheetItem {
    Public(me.sanao1006.core.model.notes.Visibility.PUBLIC, TablerIcons.World, "Public"),
    Home(me.sanao1006.core.model.notes.Visibility.HOME, TablerIcons.Home, "Home"),
    Followers(me.sanao1006.core.model.notes.Visibility.FOLLOWERS, TablerIcons.Lock, "Followers");

    companion object {
        fun getAllItems(): List<VisibilityItem> = listOf(Public, Home, Followers)
    }
}

enum class LocalOnlyItem(
    val value: Boolean,
    override val resId: Int,
    override val description: String
) : ModelSheetItem {
    LocalOnly(true, TablerIcons.Rocket, "Local Only"),
    NotLocalOnly(false, TablerIcons.RocketOff, "Not Local Only");

    companion object {
        fun getAllItems(): List<LocalOnlyItem> = listOf(LocalOnly, NotLocalOnly)
    }
}

enum class ReactionAcceptanceItem(
    val value: me.sanao1006.core.model.notes.ReactionAcceptance?,
    override val resId: Int,
    override val description: String
) : ModelSheetItem {
    All(null, TablerIcons.Icons, "All"),
    LikeOnly(
        me.sanao1006.core.model.notes.ReactionAcceptance.LIKE_ONLY,
        TablerIcons.Heart,
        "Like Only"
    ),
    LikeOnlyForRemote(
        me.sanao1006.core.model.notes.ReactionAcceptance.LIKE_ONLY_FOR_REMOTE,
        TablerIcons.HeartDiscount,
        "Like Only For Remote"
    ),
    NonSensitiveOnly(
        me.sanao1006.core.model.notes.ReactionAcceptance.NON_SENSITIVE_ONLY,
        TablerIcons.MoodKid,
        "Non Sensitive Only"
    );

    companion object {
        fun getAllItems(): List<ReactionAcceptanceItem> =
            listOf(All, LikeOnly, LikeOnlyForRemote, NonSensitiveOnly)
    }
}

data class NoteOptionState(
    val visibility: me.sanao1006.core.model.notes.Visibility,
    val localOnly: Boolean,
    val reactionAcceptance: me.sanao1006.core.model.notes.ReactionAcceptance?,
)

@ExperimentalMaterial3Api
@Composable
internal fun NoteOptionRow(
    isShowBottomSheet: Boolean,
    noteOptionContent: me.sanao1006.core.model.notes.NoteOptionContent,
    noteOptionState: NoteOptionState,
    modifier: Modifier = Modifier,
    onBottomSheetOuterClicked: () -> Unit,
    onIconClicked: (me.sanao1006.core.model.notes.NoteOptionContent) -> Unit,
    onVisibilityClicked: (me.sanao1006.core.model.notes.Visibility) -> Unit,
    onLocalOnlyClicked: (Boolean) -> Unit,
    onReactionAcceptanceClicked: (me.sanao1006.core.model.notes.ReactionAcceptance?) -> Unit
) {
    OverlayEffect(isShowBottomSheet) {
        if (isShowBottomSheet) {
            val model = when (noteOptionContent) {
                me.sanao1006.core.model.notes.NoteOptionContent.VISIBILITY -> VisibilityItem.getAllItems()
                me.sanao1006.core.model.notes.NoteOptionContent.LOCAL_ONLY -> LocalOnlyItem.getAllItems()
                me.sanao1006.core.model.notes.NoteOptionContent.REACTION_ACCEPTANCE -> ReactionAcceptanceItem.getAllItems()
            }
            show(
                BottomSheetOverlay(
                    model = model,
                    onDismiss = onBottomSheetOuterClicked,
                    content = { modelItems, _ ->
                        Column(
                            modifier = Modifier.padding(
                                top = 24.dp,
                                end = 16.dp,
                                bottom = 40.dp,
                                start = 16.dp
                            ),
                            verticalArrangement = Arrangement.spacedBy(24.dp)
                        ) {
                            modelItems.forEach {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            when (it) {
                                                is VisibilityItem -> onVisibilityClicked(it.value)
                                                is LocalOnlyItem -> onLocalOnlyClicked(it.value)
                                                is ReactionAcceptanceItem -> onReactionAcceptanceClicked(
                                                    it.value
                                                )
                                            }
                                        },
                                ) {
                                    Icon(painter = painterResource(it.resId), "")
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(text = it.description)
                                }
                            }
                        }
                    }
                )
            )
        }
    }

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val visibilityIcon = when (noteOptionState.visibility) {
            me.sanao1006.core.model.notes.Visibility.PUBLIC -> TablerIcons.World
            me.sanao1006.core.model.notes.Visibility.HOME -> TablerIcons.Home
            me.sanao1006.core.model.notes.Visibility.FOLLOWERS -> TablerIcons.Lock
            me.sanao1006.core.model.notes.Visibility.SPECIFIED -> TablerIcons.Mail
        }
        val localOnlyIcon =
            if (noteOptionState.localOnly) TablerIcons.Rocket else TablerIcons.RocketOff
        val reactionAcceptanceIcon = when (noteOptionState.reactionAcceptance) {
            me.sanao1006.core.model.notes.ReactionAcceptance.LIKE_ONLY -> TablerIcons.Heart
            me.sanao1006.core.model.notes.ReactionAcceptance.LIKE_ONLY_FOR_REMOTE -> TablerIcons.HeartDiscount
            me.sanao1006.core.model.notes.ReactionAcceptance.NON_SENSITIVE_ONLY -> TablerIcons.MoodKid
            null -> TablerIcons.Icons
        }

        IconButton(onClick = { onIconClicked(me.sanao1006.core.model.notes.NoteOptionContent.VISIBILITY) }) {
            Icon(painter = painterResource(visibilityIcon), "")
        }
        Spacer(modifier = Modifier.width(4.dp))
        IconButton(onClick = { onIconClicked(me.sanao1006.core.model.notes.NoteOptionContent.LOCAL_ONLY) }) {
            Icon(painter = painterResource(localOnlyIcon), "")
        }
        Spacer(modifier = Modifier.width(4.dp))
        IconButton(onClick = { onIconClicked(me.sanao1006.core.model.notes.NoteOptionContent.REACTION_ACCEPTANCE) }) {
            Icon(painter = painterResource(reactionAcceptanceIcon), "")
        }
        Spacer(modifier = Modifier.width(8.dp))
    }
}