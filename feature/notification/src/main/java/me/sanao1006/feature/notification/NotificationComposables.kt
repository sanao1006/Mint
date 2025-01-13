package me.sanao1006.feature.notification

import android.content.Context
import androidx.compose.foundation.Image
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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.compose.rememberAsyncImagePainter
import ir.alirezaivaz.tablericons.TablerIcons
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import me.sanao1006.core.data.util.TimeUtils.getRelativeTimeString
import me.sanao1006.core.designsystem.MintTheme
import me.sanao1006.core.model.common.User
import me.sanao1006.core.model.notes.TimelineItem
import me.sanao1006.core.model.notes.Visibility
import me.sanao1006.core.model.requestbody.account.NotificationType
import me.sanao1006.core.model.uistate.NotificationUiStateObject
import me.sanao1006.core.ui.Host
import me.sanao1006.core.ui.NoteId
import me.sanao1006.core.ui.NoteText
import me.sanao1006.core.ui.NoteUri
import me.sanao1006.core.ui.TimelineItemSection
import me.sanao1006.core.ui.UserId
import me.sanao1006.core.ui.Username
import me.snao1006.res_value.ResString

@Composable
fun NotificationColumn(
    modifier: Modifier = Modifier,
    context: Context,
    notifications: List<NotificationUiStateObject>,
    onIconClick: (String, String?, String?) -> Unit,
    onReplyClick: (NoteId, Username, Host?) -> Unit,
    onRepostClick: (NoteId) -> Unit,
    onReactionClick: (NoteId) -> Unit,
    onOptionClick: (NoteId, UserId?, Username?, Host?, NoteText, NoteUri) -> Unit
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        itemsIndexed(notifications) { index, it ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                if (index == 0) {
                    Spacer(modifier = Modifier.height(16.dp))
                }
                when (NotificationType.get(it.type)) {
                    NotificationType.REPLY, NotificationType.MENTION, NotificationType.QUOTE -> {
                        NotificationSectionMessage(
                            notificationUiState = it,
                            onIconClick = onIconClick,
                            onReplyClick = onReplyClick,
                            onRepostClick = onRepostClick,
                            onReactionClick = onReactionClick,
                            onOptionClick = onOptionClick
                        )
                    }

                    else -> {
                        NotificationSectionItem(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            notificationUiState = it,
                            context = context,
                            onIconClick = onIconClick
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
            HorizontalDivider()
        }
    }
}

@Composable
private fun NotificationSectionMessage(
    notificationUiState: NotificationUiStateObject,
    modifier: Modifier = Modifier,
    onIconClick: (String, String?, String?) -> Unit,
    onReplyClick: (NoteId, Username, Host?) -> Unit,
    onRepostClick: (NoteId) -> Unit,
    onReactionClick: (NoteId) -> Unit,
    onOptionClick: (NoteId, UserId?, Username?, Host?, NoteText, NoteUri) -> Unit
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(16.dp))
            Icon(
                painter = painterResource(
                    when (NotificationType.get(notificationUiState.type)) {
                        NotificationType.REPLY -> TablerIcons.ArrowBackUp
                        NotificationType.MENTION -> TablerIcons.At
                        else -> TablerIcons.ArrowBackUp
                    }
                ),
                contentDescription = ""
            )
            Spacer(modifier = Modifier.width(8.dp))

            notificationUiState.user?.let {
                Text(
                    text = it.name ?: it.username,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        if (notificationUiState.timelineItem != null) {
            TimelineItemSection(
                timelineItem = notificationUiState.timelineItem!!,
                onIconClick = onIconClick,
                onReplyClick = {
                    if (!notificationUiState.user?.username.isNullOrEmpty()) {
                        onReplyClick(
                            notificationUiState.timelineItem!!.id,
                            notificationUiState.timelineItem!!.user!!.username,
                            notificationUiState.user?.host
                        )
                    }
                },
                onRepostClick = { onRepostClick(notificationUiState.id) },
                onReactionClick = { onReactionClick(notificationUiState.id) },
                onOptionClick = {
                    onOptionClick(
                        notificationUiState.id,
                        notificationUiState.user?.id,
                        notificationUiState.user?.username,
                        notificationUiState.user?.host,
                        notificationUiState.timelineItem!!.text,
                        notificationUiState.timelineItem!!.uri
                    )
                }
            )
        }
    }
}

@Composable
private fun NotificationSectionItem(
    notificationUiState: NotificationUiStateObject,
    context: Context,
    modifier: Modifier = Modifier,
    onIconClick: (String, String?, String?) -> Unit
) {
    Column(modifier = modifier) {
        when (NotificationType.get(notificationUiState.type)) {
            NotificationType.REACTION -> {
                notificationUiState.timelineItem?.let {
                    ReactionItem(
                        reactions = it.reactions,
                        reactionsEmojis = it.reactionsEmojis
                    )
                }
            }

            else -> {
                NotificationIcon(notificationType = NotificationType.get(notificationUiState.type))
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        if (notificationUiState.user != null && notificationUiState.timelineItem != null) {
            NoteItem(
                user = notificationUiState.user!!,
                timelineItem = notificationUiState.timelineItem!!,
                context = context,
                createdAt = notificationUiState.createdAt,
                onIconClick = onIconClick
            )
        }
    }
}

@Composable
private fun NoteItem(
    user: User,
    timelineItem: TimelineItem,
    context: Context,
    createdAt: String,
    modifier: Modifier = Modifier,
    onIconClick: (String, String?, String?) -> Unit
) {
    Row(modifier = modifier.fillMaxWidth()) {
        Image(
            modifier = Modifier
                .size(48.dp)
                .clip(shape = CircleShape)
                .clickable {
                    onIconClick(
                        user.id,
                        user.username,
                        user.host
                    )
                },
            painter = rememberAsyncImagePainter(user.avatarUrl),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(8.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            UserInfoRow(
                user = user,
                timelineItem = timelineItem,
                context = context,
                createdAt = createdAt,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = timelineItem.text,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun UserInfoRow(
    user: User,
    timelineItem: TimelineItem,
    context: Context,
    createdAt: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Absolute.SpaceBetween
    ) {
        Text(
            text = user.name ?: user.username,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = getRelativeTimeString(context, createdAt),
                style = MaterialTheme.typography.bodyMedium
            )

            when (timelineItem.visibility) {
                Visibility.PUBLIC -> {}
                else -> {
                    Icon(
                        modifier = Modifier.size(20.dp),
                        painter = painterResource(
                            when (timelineItem.visibility) {
                                Visibility.HOME -> TablerIcons.Home
                                Visibility.FOLLOWERS -> TablerIcons.Lock
                                Visibility.SPECIFIED -> TablerIcons.Mail
                                else -> TablerIcons.Home
                            }
                        ),
                        contentDescription = ""
                    )
                }
            }
        }
    }
}

@Composable
private fun ReactionItem(reactions: JsonObject?, reactionsEmojis: JsonObject?) {
    val reaction = reactions?.keys?.last().toString()
    val emoji = reactionsEmojis?.get(
        reaction.drop(1).dropLast(1)
    )?.jsonPrimitive?.content

    emoji?.let {
        AsyncImage(
            model = it,
            contentDescription = "",
            modifier = Modifier
                .size(32.dp)
                .padding(start = 4.dp),
            contentScale = ContentScale.Fit
        )
    } ?: run {
        Text(
            text = reaction,
            fontSize = 24.sp,
            modifier = Modifier.padding(start = 4.dp)
        )
    }
}

@Composable
private fun NotificationIcon(notificationType: NotificationType) {
    Row {
        Icon(
            painter = painterResource(
                when (notificationType) {
                    NotificationType.NOTES -> TablerIcons.FileText
                    NotificationType.FOLLOWS -> TablerIcons.UserPlus
                    NotificationType.RENOTE -> TablerIcons.Repeat
                    NotificationType.POLL_ENDED -> TablerIcons.ChartInfographic
                    NotificationType.RECEIVE_FOLLOW_REQUEST -> TablerIcons.UserCheck
                    NotificationType.FOLLOW_REQUEST_ACCEPTED -> TablerIcons.UserCheck
                    NotificationType.ROLE_ASSIGNED -> TablerIcons.UserCheck
                    NotificationType.ACHIEVEMENT_EARNED -> TablerIcons.Trophy
                    NotificationType.EXPORT_COMPLETED -> TablerIcons.Download
                    NotificationType.LOGIN -> TablerIcons.Login
                    NotificationType.APP -> TablerIcons.Apps
                    NotificationType.TEST -> TablerIcons.TestPipe
                    NotificationType.POLL_VOTE -> TablerIcons.ChartBar
                    NotificationType.GROUP_INVITED -> TablerIcons.UserPlus
                    else -> TablerIcons.ExclamationMark
                }
            ),
            contentDescription = ""
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = stringResource(
                when (notificationType) {
                    NotificationType.NOTES -> ResString.notification_new_note
                    NotificationType.FOLLOWS -> ResString.notification_followed
                    NotificationType.RENOTE -> ResString.notification_renote
                    NotificationType.POLL_ENDED -> ResString.notification_poll_ended
                    NotificationType.RECEIVE_FOLLOW_REQUEST -> ResString.notification_follow_request
                    NotificationType.FOLLOW_REQUEST_ACCEPTED ->
                        ResString.notification_follow_request_accepted

                    NotificationType.ROLE_ASSIGNED -> ResString.notification_role_assigned
                    NotificationType.ACHIEVEMENT_EARNED -> ResString.notification_achievement_earned
                    NotificationType.EXPORT_COMPLETED -> ResString.notification_export_completed
                    NotificationType.LOGIN -> ResString.notification_login
                    NotificationType.APP -> ResString.notification_app
                    NotificationType.TEST -> ResString.notification_test
                    NotificationType.POLL_VOTE -> ResString.notification_poll_vote
                    NotificationType.GROUP_INVITED -> ResString.notification_group_invited
                    else -> ResString.notification_unknown
                }
            )
        )
    }
}

@Composable
@PreviewLightDark
private fun PreviewNotificationColumn() {
    MintTheme {
        val notifications = listOf<NotificationUiStateObject>(
            NotificationUiStateObject(
                id = "1",
                type = "reply",
                createdAt = "2021-09-01T00:00:00Z",
                user = User(
                    id = "1",
                    username = "user1",
                    name = "User 1",
                    avatarUrl = "https://example.com/user1.png"
                ),
                timelineItem = TimelineItem(
                    id = "1",
                    user = User(
                        id = "2",
                        username = "user2",
                        name = "User 2",
                        avatarUrl = "https://example.com/user2.png"
                    ),
                    text = "Hello, World!",
                    uri = "https://example.com/1",
                    visibility = Visibility.PUBLIC,
                    reactions = null,
                    createdAt = "2021-09-01T00:00:00Z"
                )
            ),
            NotificationUiStateObject(
                id = "2",
                type = "reaction",
                createdAt = "2021-09-01T00:00:00Z",
                user = User(
                    id = "1",
                    username = "user1",
                    name = "User 1",
                    avatarUrl = "https://example.com"
                ),
                timelineItem = TimelineItem(
                    id = "2",
                    user = User(
                        id = "2",
                        username = "user2",
                        name = "User 2",
                        avatarUrl = "https://example.com"
                    ),
                    text = "Hello, World!",
                    uri = "https://example.com/2",
                    visibility = Visibility.PUBLIC,
                    reactions = Json.parseToJsonElement(
                        """{"👍": 1}"""
                    ).jsonObject,
                    createdAt = "2021-09-01T00:00:00Z"
                )
            ),
            NotificationUiStateObject(
                id = "3",
                type = "login",
                createdAt = "2021-09-01T00:00:00Z",
                user = User(
                    id = "1",
                    username = "user1",
                    name = "User 1",
                    avatarUrl = "https://example.com"
                ),
                timelineItem = TimelineItem(
                    id = "3",
                    user = User(
                        id = "2",
                        username = "user3",
                        name = "User 3",
                        avatarUrl = "https://example.com"
                    ),
                    text = "Hello, World!",
                    uri = "https://example.com/2",
                    visibility = Visibility.PUBLIC,
                    createdAt = "2021-09-01T00:00:00Z"
                )
            )
        )
        val context = LocalContext.current
        Surface {
            NotificationColumn(
                context = context,
                notifications = notifications,
                onIconClick = { _, _, _ -> },
                onReplyClick = { _, _, _ -> },
                onRepostClick = {},
                onOptionClick = { _, _, _, _, _, _ -> },
                onReactionClick = {}
            )
        }
    }
}
