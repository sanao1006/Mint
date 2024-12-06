package me.sanao1006.feature.note

import androidx.annotation.StringRes
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.slack.circuit.overlay.OverlayEffect
import com.slack.circuitx.overlays.BottomSheetOverlay
import ir.alirezaivaz.tablericons.TablerIcons
import me.sanao1006.core.model.notes.NoteOptionContent
import me.sanao1006.core.model.notes.ReactionAcceptance
import me.sanao1006.core.model.notes.Visibility
import me.snao1006.res_value.ResString

interface ModelSheetItem {
  val resId: Int
  val description: Int
}

enum class VisibilityItem(
  val value: Visibility,
  override val resId: Int,
  @StringRes override val description: Int
) : ModelSheetItem {
  Public(
    Visibility.PUBLIC,
    TablerIcons.World,
    ResString.bottom_sheet_visibility_item_description_public
  ),
  Home(
    Visibility.HOME,
    TablerIcons.Home,
    ResString.bottom_sheet_visibility_item_description_home
  ),
  Followers(
    Visibility.FOLLOWERS,
    TablerIcons.Lock,
    ResString.bottom_sheet_visibility_item_description_followers
  );

  companion object {
    fun getAllItems(): List<VisibilityItem> = listOf(Public, Home, Followers)
  }
}

enum class LocalOnlyItem(
  val value: Boolean,
  override val resId: Int,
  @StringRes override val description: Int
) : ModelSheetItem {
  LocalOnly(
    true,
    TablerIcons.Rocket,
    ResString.bottom_sheet_local_only_item_description_local_only
  ),
  NotLocalOnly(
    false,
    TablerIcons.RocketOff,
    ResString.bottom_sheet_local_only_item_description_not_local_only
  );

  companion object {
    fun getAllItems(): List<LocalOnlyItem> = listOf(LocalOnly, NotLocalOnly)
  }
}

enum class ReactionAcceptanceItem(
  val value: ReactionAcceptance?,
  override val resId: Int,
  @StringRes override val description: Int
) : ModelSheetItem {
  All(null, TablerIcons.Icons, ResString.bottom_sheet_reaction_acceptance_item_description_all),
  LikeOnly(
    ReactionAcceptance.LIKE_ONLY,
    TablerIcons.Heart,
    ResString.bottom_sheet_reaction_acceptance_item_description_like_only
  ),
  LikeOnlyForRemote(
    ReactionAcceptance.LIKE_ONLY_FOR_REMOTE,
    TablerIcons.HeartDiscount,
    ResString.bottom_sheet_reaction_acceptance_item_description_like_only_for_remote
  ),
  NonSensitiveOnly(
    ReactionAcceptance.NON_SENSITIVE_ONLY,
    TablerIcons.MoodKid,
    ResString.bottom_sheet_reaction_acceptance_item_description_not_sensitive_only
  );

  companion object {
    fun getAllItems(): List<ReactionAcceptanceItem> =
      listOf(All, LikeOnly, LikeOnlyForRemote, NonSensitiveOnly)
  }
}

data class NoteOptionState(
  val visibility: Visibility,
  val localOnly: Boolean,
  val reactionAcceptance: ReactionAcceptance?
)

@ExperimentalMaterial3Api
@Composable
internal fun NoteOptionRow(
  isShowBottomSheet: Boolean,
  noteOptionContent: NoteOptionContent,
  noteOptionState: NoteOptionState,
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
                    }
                ) {
                  Icon(painter = painterResource(it.resId), "")
                  Spacer(modifier = Modifier.width(8.dp))
                  Text(text = stringResource(it.description))
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
      Visibility.PUBLIC -> TablerIcons.World
      Visibility.HOME -> TablerIcons.Home
      Visibility.FOLLOWERS -> TablerIcons.Lock
      Visibility.SPECIFIED -> TablerIcons.Mail
    }
    val localOnlyIcon =
      if (noteOptionState.localOnly) TablerIcons.Rocket else TablerIcons.RocketOff
    val reactionAcceptanceIcon = when (noteOptionState.reactionAcceptance) {
      ReactionAcceptance.LIKE_ONLY -> TablerIcons.Heart
      ReactionAcceptance.LIKE_ONLY_FOR_REMOTE -> TablerIcons.HeartDiscount
      ReactionAcceptance.NON_SENSITIVE_ONLY -> TablerIcons.MoodKid
      null -> TablerIcons.Icons
    }

    IconButton(onClick = { onIconClicked(NoteOptionContent.VISIBILITY) }) {
      Icon(painter = painterResource(visibilityIcon), "")
    }
    Spacer(modifier = Modifier.width(4.dp))
    IconButton(onClick = { onIconClicked(NoteOptionContent.LOCAL_ONLY) }) {
      Icon(painter = painterResource(localOnlyIcon), "")
    }
    Spacer(modifier = Modifier.width(4.dp))
    IconButton(onClick = { onIconClicked(NoteOptionContent.REACTION_ACCEPTANCE) }) {
      Icon(painter = painterResource(reactionAcceptanceIcon), "")
    }
    Spacer(modifier = Modifier.width(8.dp))
  }
}
