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
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ir.alirezaivaz.tablericons.TablerIcons

enum class TimelineItemAction {
    Renote,
    Option
}

interface TimelineItem {
    val resId: Int
    val description: Int
}

enum class RenoteItem(
    override val resId: Int,
    @StringRes override val description: Int
) : TimelineItem {
    Renote(
        TablerIcons.Repeat,
        1
    ),
    Quote(
        TablerIcons.Quote,
        1
    );

    companion object {
        fun getAllItems(): List<RenoteItem> = listOf(Renote, Quote)
    }
}

enum class OptionItem(
    override val resId: Int,
    @StringRes override val description: Int
) : TimelineItem {
    Detail(
        TablerIcons.InfoCircle,
        1
    ),
    Copy(
        TablerIcons.Copy,
        1
    ),
    CopyLink(
        TablerIcons.Link,
        1
    ),
    Share(
        TablerIcons.Share,
        1
    ),
    Favorite(
        TablerIcons.StarFilled,
        1
    )
    ;

    companion object {
        fun getAllItems(): List<OptionItem> = listOf(
            Detail,
            Copy,
            CopyLink,
            Share,
            Favorite
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimelineBottomSheet(
    isShowBottomSheet: Boolean,
    timelineItemAction: TimelineItemAction,
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    onRenoteIconCLick: (RenoteItem) -> Unit,
    onOptionIconCLick: (OptionItem) -> Unit
) {
    if (isShowBottomSheet) {
        val model = when (timelineItemAction) {
            TimelineItemAction.Renote -> RenoteItem.getAllItems()
            TimelineItemAction.Option -> OptionItem.getAllItems()
        }
        ModalBottomSheet(
            modifier = modifier,
            onDismissRequest = onDismissRequest
        ) {
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
                                    TimelineItemAction.Renote -> onRenoteIconCLick(it as RenoteItem)
                                    TimelineItemAction.Option -> onOptionIconCLick(it as OptionItem)
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
    }
}
