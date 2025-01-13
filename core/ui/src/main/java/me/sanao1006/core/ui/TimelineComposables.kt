package me.sanao1006.core.ui

import android.content.Context
import android.os.Vibrator
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.getSystemService
import coil3.compose.AsyncImage
import ir.alirezaivaz.tablericons.TablerIcons
import kotlinx.serialization.json.JsonObject
import me.sanao1006.core.data.util.TimeUtils.getRelativeTimeString
import me.sanao1006.core.data.util.vibrate
import me.sanao1006.core.designsystem.MintTheme
import me.sanao1006.core.model.common.User
import me.sanao1006.core.model.meta.Note
import me.sanao1006.core.model.notes.Instance
import me.sanao1006.core.model.notes.TimelineItem
import me.sanao1006.core.model.notes.Visibility
import me.sanao1006.core.ui.modifier.dashedBorder
import me.snao1006.res_value.ResString

typealias NoteId = String
typealias UserId = String
typealias Username = String
typealias Host = String
typealias NoteText = String
typealias NoteUri = String

@Composable
fun TimelineColumn(
    modifier: Modifier = Modifier,
    timelineItems: List<TimelineItem?>,
    onIconClick: (String, String?, String?) -> Unit,
    onReplyClick: (NoteId, Username, UserId?, NoteText, Host?) -> Unit,
    onRepostClick: (NoteId, UserId, NoteText) -> Unit,
    onReactionClick: (NoteId) -> Unit,
    onOptionClick: (NoteId, UserId?, Username?, Host?, NoteText, NoteUri) -> Unit
) {
    val context = LocalContext.current
    val vibrator = context.getSystemService<Vibrator>()

    LazyColumn(
        modifier = modifier.padding(horizontal = 4.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        itemsIndexed(timelineItems) { index, it ->
            it?.let { timelineItem ->
                val renoteItem = timelineItem.renote?.toTimelineUiState()
                val replyItem = timelineItem.reply?.toTimelineUiState()
                val isRenote = timelineItem.text.isEmpty() && renoteItem != null
                val isReply = replyItem != null
                val displayTimelineItem = when {
                    isRenote -> renoteItem
                    else -> timelineItem
                }
                ElevatedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                        .border(
                            0.6.dp,
                            MaterialTheme.colorScheme.outlineVariant,
                            RoundedCornerShape(8.dp)
                        ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp)
                ) {
                    if (isRenote) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.background)
                                .padding(top = 4.dp, start = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                modifier = Modifier.size(16.dp),
                                painter = painterResource(TablerIcons.Repeat),
                                contentDescription = ""
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            val user: String =
                                timelineItem.user?.name ?: timelineItem.user?.username ?: ""
                            Text(
                                textAlign = TextAlign.Start,
                                text = stringResource(ResString.renote_description, user),
                                style = MaterialTheme.typography.bodyMedium,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                    if (isReply) {
                        ReplySection(
                            replyItem = replyItem,
                            modifier = Modifier.fillMaxWidth(),
                            onIconClick = { id, username, host ->
                                vibrator?.vibrate()
                                onIconClick(id, username, host)
                            }
                        )
                    }
                    TimelineItemSection(
                        modifier = Modifier.fillMaxSize(),
                        timelineItem = displayTimelineItem,
                        onIconClick = { id, username, host ->
                            vibrator?.vibrate()
                            onIconClick(id, username, host)
                        },
                        onReplyClick = {
                            if (!it.user?.username.isNullOrEmpty()) {
                                vibrator?.vibrate()
                                onReplyClick(
                                    it.id,
                                    it.user?.username.orEmpty(),
                                    it.user?.id,
                                    it.text,
                                    it.user?.host
                                )
                            }
                        },
                        onRepostClick = {
                            vibrator?.vibrate()
                            onRepostClick(it.id, it.user?.id.orEmpty(), it.text)
                        },
                        onReactionClick = {
                            vibrator?.vibrate()
                            onReactionClick(it.id)
                        },
                        onOptionClick = {
                            vibrator?.vibrate()
                            onOptionClick(
                                it.id,
                                it.user?.id,
                                it.user?.username,
                                it.user?.host,
                                it.text,
                                it.uri
                            )
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun TimelineItemSection(
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
            .wrapContentWidth()
    ) {
        ListItem(
            modifier = Modifier
                .fillMaxWidth(),
            leadingContent = {
                AsyncImage(
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
                    model = timelineItem.user?.avatarUrl,
                    contentDescription = null,
                    alignment = Alignment.TopStart,
                    contentScale = ContentScale.Crop
                )
            },
            headlineContent = {
                UserNameRow(
                    timelineItem = timelineItem,
                    context = LocalContext.current,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            supportingContent = {
                val instance = timelineItem.user?.instance
                Column(modifier = Modifier.wrapContentHeight()) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        val instanceName = timelineItem.user?.host
                        val name = "@${timelineItem.user?.username}${
                            instanceName?.let {
                                "@$it"
                            } ?: ""
                        }"
                        Text(
                            modifier = Modifier.weight(1f),
                            text = name,
                            style = MaterialTheme.typography.labelMedium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        if (instance != null) {
                            InstanceInfoRow(
                                timelineItem = timelineItem,
                                modifier = Modifier.clip(CircleShape)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    NoteContent(
                        timelineItem = timelineItem,
                        modifier = Modifier
                            .fillMaxWidth()
                    )

                    val isQuote = timelineItem.text.isNotEmpty() && timelineItem.renote != null
                    if (isQuote) {
                        Spacer(modifier = Modifier.height(8.dp))
                        QuoteSection(
                            modifier = Modifier
                                .fillMaxWidth()
                                .dashedBorder(
                                    width = 1.dp,
                                    on = 2.dp,
                                    off = 2.dp,
                                    shape = RoundedCornerShape(8.dp),
                                    brush = SolidColor(MaterialTheme.colorScheme.primary)
                                ),
                            note = timelineItem.renote!!,
                            onIconClick = { id, username, host ->
                                onIconClick(id, username, host)
                            }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                    timelineItem.reactions?.let {
                        Spacer(modifier = Modifier.height(4.dp))
                        ReactionsSection(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight(),
                            reactions = it
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))
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
        )
    }
}

@Composable
private fun ReplySection(
    replyItem: TimelineItem,
    modifier: Modifier = Modifier,
    onIconClick: (String, String?, String?) -> Unit
) {
    ListItem(
        modifier = modifier,
        leadingContent = {
            AsyncImage(
                modifier = Modifier
                    .size(40.dp)
                    .clip(shape = CircleShape)
                    .clickable {
                        onIconClick(
                            replyItem.user?.id ?: "",
                            replyItem.user?.username,
                            replyItem.user?.host
                        )
                    },
                model = replyItem.user?.avatarUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
        },
        headlineContent = {
            UserNameRow(
                timelineItem = replyItem,
                context = LocalContext.current,
                modifier = Modifier.fillMaxWidth()
            )
        },
        supportingContent = {
            Text(text = replyItem.text)

            replyItem.reactions?.let {
                Spacer(modifier = Modifier.height(4.dp))
                ReactionsSection(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    reactions = it
                )
            }
        }
    )
}

@Composable
private fun QuoteSection(
    note: Note,
    modifier: Modifier = Modifier,
    onIconClick: (String, String?, String?) -> Unit
) {
    ListItem(
        modifier = modifier,
        leadingContent = {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(0.dp),
                verticalArrangement = Arrangement.Top
            ) {
                Text("hello")
                AsyncImage(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(shape = CircleShape)
                        .clickable {
                            onIconClick(
                                note.user?.id ?: "",
                                note.user?.username,
                                note.user?.host
                            )
                        },
                    model = note.user?.avatarUrl,
                    contentDescription = null,
                    alignment = Alignment.TopCenter,
                    contentScale = ContentScale.Crop
                )
            }
        },
        headlineContent = {
            UserNameRow(
                timelineItem = note.toTimelineUiState(),
                context = LocalContext.current,
                modifier = Modifier.fillMaxWidth()
            )
        },
        supportingContent = {
            note.text?.let {
                Text(text = it)
            }
        }
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ReactionsSection(
    reactions: JsonObject,
    modifier: Modifier = Modifier
) {
    val reactions = reactions.toList()
    FlowRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        reactions.forEach { (reaction, count) ->
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                    .border(1.dp, MaterialTheme.colorScheme.background, RoundedCornerShape(8.dp))
                    .padding(vertical = 4.dp, horizontal = 6.dp)

            ) {
                Text(text = reaction)
                Spacer(modifier = Modifier.width(3.dp))
                Text(text = count.toString())
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun NoteContent(
    modifier: Modifier = Modifier,
    timelineItem: TimelineItem
) {
    Column(modifier = modifier) {
        if (timelineItem.text.isNotEmpty()) {
            Text(
                text = timelineItem.text
            )
        }
        if (timelineItem.files.isNotEmpty()) {
            // Bug?: When Text is empty and there is a file, insert an empty character between the two.
            // Otherwise, the layout of timeline items will be corrupted for some reason.
            if (timelineItem.text.isEmpty()) {
                Text(
                    text = "",
                    fontSize = 0.sp,
                    lineHeight = 0.sp
                )
            }
            val size = timelineItem.files.size
            Spacer(modifier = Modifier.height(4.dp))
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                timelineItem.files.forEachIndexed { index, it ->
                    AsyncImage(
                        modifier = Modifier
                            .then(
                                if (size == 1) {
                                    Modifier.aspectRatio(1f)
                                } else {
                                    Modifier
                                        .sizeIn(maxHeight = 160.dp, maxWidth = 160.dp)
                                        .weight(0.5f)
                                }
                            )
                            .padding(bottom = 8.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        model = it.url,
                        contentDescription = null,
                        contentScale = ContentScale.Crop
                    )
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
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
            style = MaterialTheme.typography.titleSmall,
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
                style = MaterialTheme.typography.bodyMedium
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
            .wrapContentWidth()
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
                .size(12.dp)
                .clip(RoundedCornerShape(4.dp)),
            model = timelineItem.user?.instance?.faviconUrl,
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = timelineItem.user?.instance?.name ?: "",
            style = MaterialTheme.typography.bodySmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = Color.Black
        )
        Spacer(modifier = Modifier.width(4.dp))
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
        modifier = modifier.wrapContentHeight(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Icon(
            painterResource(TablerIcons.MessageCirclePlus),
            "",
            modifier = Modifier
                .size(18.dp)
                .clickable { onReplyClick() }
        )

        Icon(
            painterResource(
                if (canRenote) TablerIcons.Repeat else TablerIcons.RepeatOff
            ),
            "",
            modifier = Modifier
                .size(18.dp)
                .clickable { if (canRenote) onRepostClick() }
        )

        Icon(
            painterResource(TablerIcons.MoodPlus),
            "",
            modifier = Modifier
                .size(18.dp)
                .clickable { onReactionClick() }
        )

        Icon(
            painterResource(TablerIcons.Dots),
            "",
            modifier = Modifier
                .size(18.dp)
                .clickable { onOptionClick() }
        )
    }
}

@PreviewLightDark
@Composable
fun PreviewTimeLineItem() {
    MintTheme {
        Surface {
            TimelineItemSection(
                timelineItem = TimelineItem(
                    user = User(
                        id = "1",
                        username = "sanao1006",
                        name = "sanao1006",
                        avatarUrl = "https://avatars.githubusercontent.com/u/20736526?v=4",
                        host = "misskey.ioおおおおおおおooo",
                        instance = Instance(
                            name = "さなおすきー",
                            faviconUrl = "https://misskey.io/favicon.ico",
                            themeColor = "#808080"
                        )
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
    }
}
