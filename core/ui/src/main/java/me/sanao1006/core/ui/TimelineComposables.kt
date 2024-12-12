package me.sanao1006.core.ui

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter
import ir.alirezaivaz.tablericons.TablerIcons
import me.sanao1006.core.model.notes.TimelineUiState
import me.sanao1006.core.model.notes.User
import me.sanao1006.screens.HomeScreen

@Composable
fun TimelineColumn(
    modifier: Modifier = Modifier,
    onIconClick: (String, String?, String?) -> Unit,
    state: HomeScreen.State
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(state.uiState) {
            it?.let { timelineUiState ->
                TimelineItem(
                    onIconClick = onIconClick,
                    timelineUiState = timelineUiState
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
    timelineUiState: TimelineUiState
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Row {
            Image(
                modifier = Modifier
                    .size(40.dp)
                    .clip(shape = CircleShape)
                    .clickable {
                        onIconClick(
                            timelineUiState.user?.id ?: "",
                            timelineUiState.user?.username,
                            timelineUiState.user?.host
                        )
                    },
                painter = rememberAsyncImagePainter(timelineUiState.user?.avatarUrl),
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(8.dp))
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = timelineUiState.user?.name ?: timelineUiState.user?.username ?: "",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.width(4.dp))
                val username =
                    timelineUiState.user?.username?.takeIf { it.isNotBlank() }?.let { "@$it" } ?: ""
                val host =
                    timelineUiState.user?.host?.takeIf { it.isNotBlank() }?.let { "@$it" } ?: ""

                if (username.isNotEmpty() && host.isNotEmpty()) {
                    Text(
                        text = "$username$host",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(2.dp))
        Text(text = timelineUiState.text)
        Spacer(modifier = Modifier.height(8.dp))
        TimelineActionRow(
            modifier = Modifier.fillMaxWidth(),
            onReplyClick = {},
            onRepostClick = {},
            onReactionClick = {}
        ) {
        }
    }
}

@Composable
private fun TimelineActionRow(
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
            Icon(painterResource(TablerIcons.MessageCirclePlus), "", modifier = Modifier.size(22.dp))
        }

        IconButton(onClick = onRepostClick) {
            Icon(painterResource(TablerIcons.Repeat), "", modifier = Modifier.size(22.dp))
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
        timelineUiState = TimelineUiState(
            user = User(
                name = "sanao1006",
                avatarUrl = ""
            ),
            text = "Hello, World!"
        ),
        onIconClick = { _, _, _ -> }
    )
}
