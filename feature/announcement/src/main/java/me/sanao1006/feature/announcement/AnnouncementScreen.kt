package me.sanao1006.feature.announcement

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ContainedLoadingIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.slack.circuit.codegen.annotations.CircuitInject
import dagger.hilt.components.SingletonComponent
import ir.alirezaivaz.tablericons.TablerIcons
import me.sanao1006.core.data.util.TimeUtils
import me.sanao1006.core.model.meta.Announcement
import me.sanao1006.core.model.uistate.AnnouncementUiState
import me.sanao1006.core.ui.DrawerItem
import me.sanao1006.core.ui.DrawerItemScreenWrapper
import me.sanao1006.screens.AnnouncementScreen
import me.snao1006.res_value.ResString

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
@CircuitInject(AnnouncementScreen::class, SingletonComponent::class)
fun AnnouncementScreen(state: AnnouncementScreen.State, modifier: Modifier) {
    Box(modifier = modifier.fillMaxSize()) {
        DrawerItemScreenWrapper(
            drawerItem = DrawerItem.ANNOUNCEMENT,
            globalIconEventSink = state.globalIconEventSink
        ) {
            Column(modifier = it.fillMaxSize()) {
                when (state.uiState) {
                    is AnnouncementUiState.Loading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            ContainedLoadingIndicator()
                        }
                    }

                    is AnnouncementUiState.Failed -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(stringResource(ResString.no_contents))
                        }
                    }

                    is AnnouncementUiState.Success -> {
                        val announcements =
                            (state.uiState as AnnouncementUiState.Success).announcements
                        AnnouncementScreenUiContent(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 8.dp),
                            announcements = announcements
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AnnouncementScreenUiContent(
    announcements: List<Announcement>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        items(
            items = announcements,
            key = { it.id }
        ) { announcement ->
            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                onClick = { }
            ) {
                AnnouncementItemSection(
                    announcementTitle = announcement.title,
                    announcementIconRes = AnnouncementIcon.getIcon(announcement.icon),
                    announcementText = announcement.text,
                    announcementCreatedAt = TimeUtils.formatCreatedAt(announcement.createdAt),
                    modifier = Modifier.padding(12.dp)
                )
            }
        }
    }
}

@Composable
private fun AnnouncementItemSection(
    announcementTitle: String,
    announcementIconRes: Int,
    announcementText: String,
    announcementCreatedAt: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Icon(
                painter = painterResource(announcementIconRes),
                contentDescription = ""
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = announcementTitle,
                fontWeight = FontWeight.SemiBold,
                overflow = TextOverflow.Ellipsis
            )
        }
        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = announcementText,
            maxLines = 5,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = announcementCreatedAt,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.outline
        )
    }
}

enum class AnnouncementIcon(val value: String, @DrawableRes val iconRes: Int) {
    INFO("info", TablerIcons.InfoCircle),
    WARNING("warning", TablerIcons.AlertTriangle),
    ERROR("error", TablerIcons.XboxX),
    SUCCESS("success", TablerIcons.CircleCheck);

    companion object {
        fun getIcon(value: String): Int =
            AnnouncementIcon.entries.find { it.value == value }?.iconRes ?: INFO.iconRes
    }
}
