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
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.slack.circuit.codegen.annotations.CircuitInject
import dagger.hilt.components.SingletonComponent
import ir.alirezaivaz.tablericons.TablerIcons
import me.sanao1006.core.data.util.TimeUtils
import me.sanao1006.core.model.meta.Announcement
import me.sanao1006.core.model.uistate.AnnouncementUiState
import me.sanao1006.core.ui.DrawerItem
import me.sanao1006.core.ui.DrawerItemScreenWrapper
import me.sanao1006.core.ui.NoContentsPlaceHolder
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
                        NoContentsPlaceHolder()
                    }

                    is AnnouncementUiState.Success -> {
                        AnnouncementScreenUiContent(
                            state = state,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AnnouncementScreenUiContent(
    state: AnnouncementScreen.State,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        var selectedTabIndex by remember { mutableIntStateOf(0) }
        TabRow(selectedTabIndex = selectedTabIndex) {
            Tab(
                selected = selectedTabIndex == 0,
                onClick = {
                    state.eventSink(AnnouncementScreen.Event.OnTabClicked(0))
                    selectedTabIndex = 0
                },
                text = { Text("Current Announcements") }
            )
            Tab(
                selected = selectedTabIndex == 1,
                onClick = {
                    state.eventSink(AnnouncementScreen.Event.OnTabClicked(1))
                    selectedTabIndex = 1
                },
                text = { Text("Past Announcements") }
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        val announcements = (state.uiState as AnnouncementUiState.Success).announcements
        if (announcements.isEmpty()) {
            NoContentsPlaceHolder()
        } else {
            AnnouncementsView(
                announcements = announcements,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
            )
        }
    }
}

@Composable
private fun AnnouncementsView(
    announcements: List<Announcement>,
    modifier: Modifier = Modifier
) {
    val expandedStates = remember { mutableStateMapOf<String, Boolean>() }
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        items(
            items = announcements,
            key = { it.id }
        ) { announcement ->
            val isExpanded = expandedStates[announcement.id] ?: false
            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    expandedStates[announcement.id] = !isExpanded
                }
            ) {
                AnnouncementItemSection(
                    announcementTitle = announcement.title,
                    announcementIconRes = AnnouncementIcon.getIcon(announcement.icon),
                    announcementImageUrl = announcement.imageUrl,
                    announcementText = announcement.text,
                    announcementCreatedAt = announcement.createdAt,
                    announcementUpdatedAt = announcement.updatedAt,
                    isExpanded = isExpanded,
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
    announcementImageUrl: String?,
    announcementCreatedAt: String,
    announcementUpdatedAt: String?,
    isExpanded: Boolean,
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
            maxLines = if (isExpanded) Int.MAX_VALUE else 5,
            overflow = if (isExpanded) TextOverflow.Clip else TextOverflow.Ellipsis
        )
        announcementImageUrl?.let {
            Spacer(modifier = Modifier.height(4.dp))
            AsyncImage(
                model = it,
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(
                ResString.announcement_created_at,
                TimeUtils.formatCreatedAt(announcementCreatedAt)
            ),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.outline
        )
        announcementUpdatedAt?.let {
            Text(
                text = stringResource(
                    ResString.announcement_created_at,
                    TimeUtils.formatCreatedAt(it)
                ),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.outline
            )
        }
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
