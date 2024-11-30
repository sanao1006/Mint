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
import me.sanao1006.core.model.home.notes.ReactionAcceptance
import me.sanao1006.core.model.home.notes.Visibility

// 共通インターフェースの定義
interface ModelSheetItem {
    val resId: Int
    val description: String
}

// 各enumクラスでインターフェースを実装
enum class VisibilityItem(
    val value: Visibility,
    override val resId: Int,
    override val description: String
) : ModelSheetItem {
    Public(Visibility.PUBLIC, TablerIcons.World, "Public"),
    Home(Visibility.HOME, TablerIcons.Home, "Home"),
    Followers(Visibility.FOLLOWERS, TablerIcons.Lock, "Followers");

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
    val value: ReactionAcceptance?,
    override val resId: Int,
    override val description: String
) : ModelSheetItem {
    All(null, TablerIcons.Hearts, "All"),
    LikeOnly(ReactionAcceptance.LIKE_ONLY, TablerIcons.Heart, "Like Only"),
    LikeOnlyForRemote(
        ReactionAcceptance.LIKE_ONLY_FOR_REMOTE,
        TablerIcons.Heartbeat,
        "Like Only For Remote"
    ),
    NonSensitiveOnly(ReactionAcceptance.NON_SENSITIVE_ONLY, TablerIcons.Seo, "Non Sensitive Only");

    companion object {
        fun getAllItems(): List<ReactionAcceptanceItem> =
            listOf(All, LikeOnly, LikeOnlyForRemote, NonSensitiveOnly)
    }
}

@ExperimentalMaterial3Api
@Composable
internal fun NoteOptionRow(
    isShowBottomSheet: Boolean,
    noteOptionContent: NoteOptionContent,
    modifier: Modifier = Modifier,
    onBottomSheetOuterClicked: () -> Unit,
    onIconClicked: (NoteOptionContent) -> Unit,
    onVisibilityClicked: (Visibility) -> Unit,
    onLocalOnlyClicked: (Boolean) -> Unit,
    onReactionAcceptanceClicked: (ReactionAcceptance?) -> Unit
) {
    OverlayEffect(isShowBottomSheet) {
        if (isShowBottomSheet) {
            val model = when (noteOptionContent) {
                NoteOptionContent.VISIBILITY -> VisibilityItem.getAllItems()
                NoteOptionContent.LOCAL_ONLY -> LocalOnlyItem.getAllItems()
                NoteOptionContent.REACTION_ACCEPTANCE -> ReactionAcceptanceItem.getAllItems()
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
        IconButton(onClick = { onIconClicked(NoteOptionContent.VISIBILITY) }) {
            Icon(painter = painterResource(TablerIcons.World), "")
        }
        Spacer(modifier = Modifier.width(4.dp))
        IconButton(onClick = { onIconClicked(NoteOptionContent.LOCAL_ONLY) }) {
            Icon(painter = painterResource(TablerIcons.Rocket), "")
        }
        Spacer(modifier = Modifier.width(4.dp))
        IconButton(onClick = { onIconClicked(NoteOptionContent.REACTION_ACCEPTANCE) }) {
            Icon(painter = painterResource(TablerIcons.Icons), "")
        }
        Spacer(modifier = Modifier.width(8.dp))
    }
}