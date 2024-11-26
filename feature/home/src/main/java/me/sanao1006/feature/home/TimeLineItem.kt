package me.sanao1006.feature.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter
import me.sanao1006.core.model.home.notes.TimelineUiState
import me.sanao1006.core.model.home.notes.User

@Composable
fun TimeLineItem(
    modifier: Modifier = Modifier,
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
                    .clip(shape = CircleShape),
                painter = rememberAsyncImagePainter(timelineUiState.user?.avatarUrl),
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = timelineUiState.user?.name ?: "",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold
            )
        }
        Spacer(modifier = Modifier.height(2.dp))
        Text(text = timelineUiState.text ?: "")
    }
}

@PreviewLightDark
@Composable
fun PreviewTimeLineItem() {
    TimeLineItem(
        timelineUiState = TimelineUiState(
            user = User(
                name = "sanao1006",
                avatarUrl = ""
            ),
            text = "Hello, World!"
        )
    )
}