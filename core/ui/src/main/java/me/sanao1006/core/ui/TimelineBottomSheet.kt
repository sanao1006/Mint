package me.sanao1006.core.ui

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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.slack.circuit.overlay.OverlayEffect
import com.slack.circuitx.overlays.BottomSheetOverlay
import ir.alirezaivaz.tablericons.TablerIcons
import me.sanao1006.core.model.uistate.TimelineItemAction
import me.snao1006.res_value.ResString

interface TimelineActionIcon {
    val resId: Int
    val description: Int
}

enum class RenoteActionIcon(
    override val resId: Int,
    @StringRes override val description: Int
) : TimelineActionIcon {
    Renote(
        TablerIcons.Repeat,
        ResString.description_renote
    ),
    Quote(
        TablerIcons.Quote,
        ResString.description_quote
    );

    companion object {
        fun getAllItems(): List<RenoteActionIcon> = listOf(Renote, Quote)
    }
}

enum class OptionActionIcon(
    override val resId: Int,
    @StringRes override val description: Int
) : TimelineActionIcon {
    Detail(
        TablerIcons.InfoCircle,
        ResString.description_detail
    ),
    Copy(
        TablerIcons.Copy,
        ResString.description_copy
    ),
    CopyLink(
        TablerIcons.Link,
        ResString.description_copy_link
    ),
    Share(
        TablerIcons.Share,
        ResString.description_share
    ),
    Favorite(
        TablerIcons.Star,
        ResString.description_favorite
    ),
    UnFavorite(
        TablerIcons.StarOff,
        ResString.description_remove_favorite
    )
    ;

    companion object {
        fun getAllItems(isFavorite: Boolean): List<OptionActionIcon> = listOf(
            Detail,
            Copy,
            CopyLink,
            Share,
            if (isFavorite) UnFavorite else Favorite
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimelineBottomSheet(
    isShowBottomSheet: Boolean = false,
    timelineItemAction: TimelineItemAction,
    isFavorite: Boolean = false,
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    onRenoteIconCLick: (RenoteActionIcon) -> Unit,
    onOptionIconCLick: (OptionActionIcon) -> Unit
) {
    OverlayEffect(
        isShowBottomSheet
    ) {
        if (isShowBottomSheet) {
            val model = when (timelineItemAction) {
                TimelineItemAction.Renote -> RenoteActionIcon.getAllItems()
                TimelineItemAction.Option -> OptionActionIcon.getAllItems(isFavorite)
                TimelineItemAction.None -> emptyList()
            }
            show(
                BottomSheetOverlay(
                    model = model,
                    onDismiss = onDismissRequest,
                    content = { modelItems, overlayNavigator ->
                        Column(
                            modifier = Modifier.padding(
                                vertical = 24.dp,
                                horizontal = 16.dp
                            ),
                            verticalArrangement = Arrangement.spacedBy(24.dp)
                        ) {
                            model.forEach {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            when (timelineItemAction) {
                                                TimelineItemAction.Renote -> {
                                                    overlayNavigator.finish(Unit)
                                                    onRenoteIconCLick(
                                                        it as RenoteActionIcon
                                                    )
                                                }

                                                TimelineItemAction.Option -> onOptionIconCLick(
                                                    it as OptionActionIcon
                                                )

                                                TimelineItemAction.None -> {
                                                }
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
}
