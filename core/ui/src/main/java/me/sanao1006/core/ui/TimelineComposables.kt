package me.sanao1006.core.ui

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.rememberAsyncImagePainter
import ir.alirezaivaz.tablericons.TablerIcons
import me.sanao1006.core.model.notes.TimelineItem
import me.sanao1006.core.model.notes.User
import me.sanao1006.core.model.notes.Visibility

typealias Id = String
typealias UserId = String
typealias Username = String
typealias Host = String
typealias Uri = String
typealias OnOptionClick = (Id, UserId?, Username?, Host?, Uri) -> Unit

@Composable
fun TimelineColumn(
    modifier: Modifier = Modifier,
    timelineItems: List<TimelineItem?>,
    onIconClick: (String, String?, String?) -> Unit,
    onReplyClick: (String, String, String?) -> Unit,
    onRepostClick: (String) -> Unit,
    onReactionClick: (String) -> Unit,
    onOptionClick: OnOptionClick
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
    Column(modifier = modifier) {
        UserNameRow(
            timelineItem = timelineItem,
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
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.Bottom
    ) {
        Text(
            text = timelineItem.user?.name ?: timelineItem.user?.username ?: "",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.width(4.dp))
        val username =
            timelineItem.user?.username?.takeIf { it.isNotBlank() }?.let { "@$it" }
                ?: ""
        val host =
            timelineItem.user?.host?.takeIf { it.isNotBlank() }?.let { "@$it" } ?: ""

        if (username.isNotEmpty() && host.isNotEmpty()) {
            Text(
                text = "$username$host",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyMedium
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        val icon = when (timelineItem.visibility) {
            Visibility.PUBLIC -> null
            Visibility.HOME -> TablerIcons.Home
            Visibility.FOLLOWERS -> TablerIcons.Lock
            Visibility.SPECIFIED -> TablerIcons.Mail
        }
        icon?.let {
            Icon(
                modifier = Modifier.size(20.dp),
                painter = painterResource(it),
                contentDescription = ""
            )
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
            overflow = TextOverflow.Ellipsis
        )
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
            ""
        ),
        onIconClick = { _, _, _ -> },
        onReplyClick = {},
        onRepostClick = {},
        onReactionClick = {},
        onOptionClick = {}
    )
}
