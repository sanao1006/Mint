package me.sanao1006.core.ui

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.rememberAsyncImagePainter
import ir.alirezaivaz.tablericons.TablerIcons
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import me.sanao1006.core.designsystem.LocalMintColors
import me.sanao1006.core.model.notes.TimelineItem
import me.sanao1006.core.model.notes.User
import me.sanao1006.core.model.notes.Visibility
import me.snao1006.res_value.ResString

typealias NoteId = String
typealias UserId = String
typealias Username = String
typealias Host = String
typealias Uri = String

@Composable
fun TimelineColumn(
    modifier: Modifier = Modifier,
    timelineItems: List<TimelineItem?>,
    onIconClick: (String, String?, String?) -> Unit,
    onReplyClick: (NoteId, Username, Host?) -> Unit,
    onRepostClick: (NoteId) -> Unit,
    onReactionClick: (NoteId) -> Unit,
    onOptionClick: (NoteId, UserId?, Username?, Host?, Uri) -> Unit
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        itemsIndexed(timelineItems) { index, it ->
            it?.let { timelineUiState ->
                TimelineItem(
                    modifier = Modifier
                        .padding(
                            top = if (index == 0) {
                                16.dp
                            } else {
                                0.dp
                            },
                            start = 16.dp,
                            end = 16.dp
                        ),
                    timelineItem = timelineUiState,
                    onIconClick = onIconClick,
                    onReplyClick = {
                        if (!it.user?.username.isNullOrEmpty()) {
                            onReplyClick(it.id, it.user?.username.orEmpty(), it.user?.host)
                        }
                    },
                    onRepostClick = { onRepostClick(it.id) },
                    onReactionClick = { onReactionClick(it.id) },
                    onOptionClick = {
                        onOptionClick(
                            it.id,
                            it.user?.id,
                            it.user?.username,
                            it.user?.host,
                            it.uri
                        )
                    }
                )
                HorizontalDivider()
            }
        }
    }
}

@Composable
private fun TimelineItem(
    modifier: Modifier = Modifier,
    onIconClick: (String, String?, String?) -> Unit,
    onReplyClick: () -> Unit,
    onRepostClick: () -> Unit,
    onReactionClick: () -> Unit,
    onOptionClick: () -> Unit,
    timelineItem: TimelineItem
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        TimelineUserInfoRow(
            timelineItem = timelineItem,
            modifier = Modifier.fillMaxWidth(),
            onIconClick = onIconClick
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(text = timelineItem.text)
        Spacer(modifier = Modifier.height(8.dp))
        val canRenote =
            timelineItem.visibility == Visibility.PUBLIC ||
                timelineItem.visibility == Visibility.HOME
        TimelineActionRow(
            canRenote = canRenote,
            modifier = Modifier.fillMaxWidth(),
            onReplyClick = onReplyClick,
            onRepostClick = onRepostClick,
            onReactionClick = onReactionClick,
            onOptionClick = onOptionClick
        )
    }
}

@Composable
private fun TimelineUserInfoRow(
    timelineItem: TimelineItem,
    modifier: Modifier = Modifier,
    onIconClick: (String, String?, String?) -> Unit
) {
    Row(modifier = modifier) {
        // user icon
        Image(
            modifier = Modifier
                .size(40.dp)
                .clip(shape = CircleShape)
                .clickable {
                    onIconClick(
                        timelineItem.user?.id ?: "",
                        timelineItem.user?.username,
                        timelineItem.user?.host
                    )
                },
            painter = rememberAsyncImagePainter(timelineItem.user?.avatarUrl),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(8.dp))
        UserInfoColumn(
            timelineItem = timelineItem,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun UserInfoColumn(
    timelineItem: TimelineItem,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    Column(modifier = modifier) {
        UserNameRow(
            timelineItem = timelineItem,
            context = context,
            modifier = Modifier.fillMaxWidth()
        )

        val instance = timelineItem.user?.instance
        if (instance != null) {
            InstanceInfoRow(
                timelineItem = timelineItem,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(4.dp))
            )
        }
    }
}

@Composable
private fun UserNameRow(
    timelineItem: TimelineItem,
    context: Context,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.Bottom
    ) {
        Text(
            text = timelineItem.user?.name ?: timelineItem.user?.username ?: "",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.width(4.dp))
        val icon = when (timelineItem.visibility) {
            Visibility.PUBLIC -> null
            Visibility.HOME -> TablerIcons.Home
            Visibility.FOLLOWERS -> TablerIcons.Lock
            Visibility.SPECIFIED -> TablerIcons.Mail
        }
        Row {
            Text(
                text = getRelativeTimeString(context, timelineItem.createdAt),
                style = MaterialTheme.typography.bodyMedium,
                color = LocalMintColors.current.onBackground
            )

            icon?.let {
                Spacer(modifier = Modifier.width(2.dp))
                Icon(
                    modifier = Modifier.size(20.dp),
                    painter = painterResource(it),
                    contentDescription = ""
                )
            }
        }
    }
}

@Composable
private fun InstanceInfoRow(
    timelineItem: TimelineItem,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .then(
                if (!timelineItem.user?.instance?.themeColor.isNullOrEmpty()) {
                    Modifier.background(
                        Color(
                            android.graphics.Color.parseColor(
                                timelineItem.user?.instance?.themeColor!!
                            )
                        )
                    )
                } else {
                    Modifier
                }
            )
    ) {
        Spacer(modifier = Modifier.width(4.dp))
        AsyncImage(
            modifier = Modifier
                .size(16.dp)
                .clip(RoundedCornerShape(4.dp)),
            model = timelineItem.user?.instance?.faviconUrl,
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = timelineItem.user?.instance?.name ?: "",
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = LocalMintColors.current.background
        )
    }
}

private fun getRelativeTimeString(context: Context, isoDateString: String): String {
    val dateTime = Instant.parse(isoDateString).toLocalDateTime(TimeZone.currentSystemDefault())
    val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

    val yearsBetween = now.year - dateTime.year
    val monthsBetween = (now.year - dateTime.year) * 12 + (now.monthNumber - dateTime.monthNumber)
    val daysBetween = now.date.dayOfYear - dateTime.date.dayOfYear
    val hoursBetween = now.hour - dateTime.hour
    val minutesBetween = now.minute - dateTime.minute
    return when {
        yearsBetween > 0 -> context.getString(ResString.date_year_ago, yearsBetween.toString())
        monthsBetween > 0 -> context.getString(ResString.date_month_ago, monthsBetween.toString())
        daysBetween > 0 -> context.getString(ResString.date_day_ago, daysBetween.toString())
        hoursBetween > 0 -> context.getString(ResString.date_hour_ago, hoursBetween.toString())
        minutesBetween > 0 -> context.getString(
            ResString.date_minute_ago,
            minutesBetween.toString()
        )

        else -> context.getString(ResString.date_now)
    }
}

@Composable
private fun TimelineActionRow(
    canRenote: Boolean,
    modifier: Modifier = Modifier,
    onReplyClick: () -> Unit,
    onRepostClick: () -> Unit,
    onReactionClick: () -> Unit,
    onOptionClick: () -> Unit
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = onReplyClick) {
            Icon(
                painterResource(TablerIcons.MessageCirclePlus),
                "",
                modifier = Modifier.size(22.dp)
            )
        }

        IconButton(onClick = { if (canRenote) onRepostClick() else Unit }) {
            Icon(
                painterResource(
                    if (canRenote) TablerIcons.Repeat else TablerIcons.RepeatOff
                ),
                "",
                modifier = Modifier.size(22.dp)
            )
        }

        IconButton(onClick = onReactionClick) {
            Icon(painterResource(TablerIcons.MoodPlus), "", modifier = Modifier.size(22.dp))
        }

        IconButton(onClick = onOptionClick) {
            Icon(painterResource(TablerIcons.Dots), "", modifier = Modifier.size(22.dp))
        }
    }
}

@PreviewLightDark
@Composable
fun PreviewTimeLineItem() {
    TimelineItem(
        timelineItem = TimelineItem(
            user = User(
                name = "sanao1006",
                avatarUrl = ""
            ),
            text = "Hello, World!",
            id = "1",
            visibility = Visibility.get("public"),
            "",
            "2024-12-14T08:58:55.689Z"
        ),
        onIconClick = { _, _, _ -> },
        onReplyClick = {},
        onRepostClick = {},
        onReactionClick = {},
        onOptionClick = {}
    )
}
