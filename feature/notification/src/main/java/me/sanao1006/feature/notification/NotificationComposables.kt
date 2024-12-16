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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.compose.rememberAsyncImagePainter
import ir.alirezaivaz.tablericons.TablerIcons
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonPrimitive
import me.sanao1006.core.data.util.getRelativeTimeString
import me.sanao1006.core.designsystem.LocalMintColors
import me.sanao1006.core.model.notes.Visibility
import me.sanao1006.core.model.requestbody.account.NotificationType
import me.sanao1006.core.model.uistate.NotificationUiStateObject
import me.sanao1006.core.ui.Host
import me.sanao1006.core.ui.NoteId
import me.sanao1006.core.ui.TimelineItemSection
import me.sanao1006.core.ui.Uri
import me.sanao1006.core.ui.UserId
import me.sanao1006.core.ui.Username

@Composable
fun NotificationColumn(
    modifier: Modifier = Modifier,
    context: Context,
    notifications: List<NotificationUiStateObject>,
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
        itemsIndexed(notifications) { index, it ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
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

                    NotificationType.REACTION -> {
                        NotificationSectionReaction(
                            notificationUiState = it,
                            context = context,
                            onIconClick = onIconClick
                        )
                    }

                    else -> {
                        NotificationSectionOthers(
                            notificationUiState = it,
                            context = context,
                            onIconClick = onIconClick
                        )
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
    onOptionClick: (NoteId, UserId?, Username?, Host?, Uri) -> Unit
) {
    Column(modifier = modifier) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Icon(
                modifier = Modifier.padding(start = 16.dp),
                painter = painterResource(
                    when (NotificationType.get(notificationUiState.type)) {
                        NotificationType.REPLY -> TablerIcons.ArrowBackUp
                        NotificationType.MENTION -> TablerIcons.At
                        else -> TablerIcons.ArrowBackUp
                    }
                ),
                contentDescription = ""
            )
            Spacer(modifier = Modifier.width(4.dp))

            Text(
                text = notificationUiState.user.name ?: notificationUiState.user.username,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )
        }
        Spacer(modifier = Modifier.height(4.dp))

        TimelineItemSection(
            modifier = Modifier
                .padding(
                    start = 16.dp,
                    end = 16.dp
                ),
            timelineItem = notificationUiState.timelineItem,
            onIconClick = onIconClick,
            onReplyClick = {
                if (notificationUiState.user.username.isNotEmpty()) {
                    onReplyClick(
                        notificationUiState.id,
                        notificationUiState.user.username,
                        notificationUiState.user.host
                    )
                }
            },
            onRepostClick = { onRepostClick(notificationUiState.id) },
            onReactionClick = { onReactionClick(notificationUiState.id) },
            onOptionClick = {
                onOptionClick(
                    notificationUiState.id,
                    notificationUiState.user.id,
                    notificationUiState.user.username,
                    notificationUiState.user.host,
                    notificationUiState.timelineItem.uri
                )
            }
        )
    }
}

@Composable
private fun NotificationSectionReaction(
    notificationUiState: NotificationUiStateObject,
    context: Context,
    modifier: Modifier = Modifier,
    onIconClick: (String, String?, String?) -> Unit
) {
    Column(modifier = modifier) {
        ReactionItem(
            reactions = notificationUiState.timelineItem.reactions,
            reactionsEmojis = notificationUiState.timelineItem.reactionsEmojis
        )
        Spacer(modifier = Modifier.height(4.dp))
        UserInfoRow(
            notificationUiState = notificationUiState,
            context = context,
            onIconClick = onIconClick
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = notificationUiState.timelineItem.text,
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun NotificationSectionOthers(
    notificationUiState: NotificationUiStateObject,
    context: Context,
    modifier: Modifier = Modifier,
    onIconClick: (String, String?, String?) -> Unit
) {
    NotificationIcon(NotificationType.get(notificationUiState.type))
    Column(modifier = modifier) {
        Spacer(modifier = Modifier.height(4.dp))
        Column(
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                .fillMaxWidth()
        ) {
            UserInfoRow(
                notificationUiState = notificationUiState,
                context = context,
                onIconClick = onIconClick
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = notificationUiState.timelineItem.text,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun UserInfoRow(
    notificationUiState: NotificationUiStateObject,
    context: Context,
    modifier: Modifier = Modifier,
    onIconClick: (String, String?, String?) -> Unit
) {
    Row(modifier = modifier.fillMaxWidth()) {
        Image(
            modifier = Modifier
                .size(40.dp)
                .clip(shape = CircleShape)
                .clickable {
                    onIconClick(
                        notificationUiState.user.id,
                        notificationUiState.user.username,
                        notificationUiState.user.host
                    )
                },
            painter = rememberAsyncImagePainter(notificationUiState.user.avatarUrl),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(8.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Text(
                text = notificationUiState.user.name ?: notificationUiState.user.username,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            val host = if (notificationUiState.user.host.isNullOrEmpty()) {
                null
            } else {
                "@${notificationUiState.user.host}"
            }
            Text(text = "@${notificationUiState.user.username}$host")
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = getRelativeTimeString(context, notificationUiState.createdAt),
                style = MaterialTheme.typography.bodyMedium,
                color = LocalMintColors.current.onBackground
            )

            when (notificationUiState.timelineItem.visibility) {
                Visibility.PUBLIC -> {}
                else -> {
                    Icon(
                        modifier = Modifier.size(20.dp),
                        painter = painterResource(
                            when (notificationUiState.timelineItem.visibility) {
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
}