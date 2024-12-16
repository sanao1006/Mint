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
import kotlinx.serialization.json.jsonPrimitive
import me.sanao1006.core.data.util.getRelativeTimeString
import me.sanao1006.core.designsystem.LocalMintColors
import me.sanao1006.core.model.notes.Visibility
import me.sanao1006.core.model.uistate.NotificationUiStateObject
import me.sanao1006.core.ui.Host
import me.sanao1006.core.ui.NoteId
import me.sanao1006.core.ui.TimelineItem
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
            when (it.type) {
                "reply", "mention" -> {
                    Column {
                        if (index == 0) {
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Icon(
                                modifier = Modifier.padding(start = 16.dp),
                                painter = painterResource(
                                    when (it.type) {
                                        "reply" -> TablerIcons.ArrowBackUp
                                        "mention" -> TablerIcons.At
                                        else -> TablerIcons.ArrowBackUp
                                    }
                                ),
                                contentDescription = ""
                            )
                            Spacer(modifier = Modifier.width(4.dp))

                            Text(
                                text = it.user.name ?: it.user.username,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.weight(1f)
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))

                        TimelineItem(
                            modifier = Modifier
                                .padding(
                                    start = 16.dp,
                                    end = 16.dp
                                ),
                            timelineItem = it.timelineItem,
                            onIconClick = onIconClick,
                            onReplyClick = {
                                if (it.user.username.isNotEmpty()) {
                                    onReplyClick(it.id, it.user.username, it.user.host)
                                }
                            },
                            onRepostClick = { onRepostClick(it.id) },
                            onReactionClick = { onReactionClick(it.id) },
                            onOptionClick = {
                                onOptionClick(
                                    it.id,
                                    it.user.id,
                                    it.user.username,
                                    it.user.host,
                                    it.timelineItem.uri
                                )
                            }
                        )
                    }
                }

                else -> {
                    Column(
                        modifier = Modifier
                            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                            .fillMaxWidth()
                    ) {
                        val re = it.timelineItem.reactions?.keys?.last().toString()
                        val emoji =
                            it.timelineItem.reactionsEmojis?.get(
                                re.drop(1).dropLast(1)
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
                                text = re,
                                fontSize = 24.sp,
                                modifier = Modifier.padding(start = 4.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Image(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(shape = CircleShape)
                                    .clickable {
                                        onIconClick(
                                            it.user.id,
                                            it.user.username,
                                            it.user.host
                                        )
                                    },
                                painter = rememberAsyncImagePainter(it.user.avatarUrl),
                                contentDescription = null,
                                contentScale = ContentScale.Crop
                            )

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f)
                            ) {
                                Text(
                                    text = it.user.name ?: it.user.username,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.SemiBold,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )

                                val host = if (it.user.host.isNullOrEmpty()) {
                                    null
                                } else {
                                    "@${it.user.host}"
                                }
                                Text(text = "@${it.user.username}$host")
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = getRelativeTimeString(context, it.createdAt),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = LocalMintColors.current.onBackground
                                )

                                when (it.timelineItem.visibility) {
                                    Visibility.PUBLIC -> {}
                                    else -> {
                                        Icon(
                                            modifier = Modifier.size(20.dp),
                                            painter = painterResource(
                                                when (it.timelineItem.visibility) {
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
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = it.timelineItem.text,
                            style = MaterialTheme.typography.bodyMedium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
            HorizontalDivider()
        }
    }
}
