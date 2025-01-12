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
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.slack.circuit.codegen.annotations.CircuitInject
import dagger.hilt.components.SingletonComponent
import ir.alirezaivaz.tablericons.TablerIcons
import me.sanao1006.core.data.util.TimeUtils
import me.sanao1006.core.designsystem.MintTheme
import me.sanao1006.core.model.meta.Announcement
import me.sanao1006.core.model.uistate.AnnouncementUiState
import me.sanao1006.core.ui.DrawerItem
import me.sanao1006.core.ui.DrawerItemScreenWrapper
import me.sanao1006.core.ui.common.ContentLoadingIndicator
import me.sanao1006.core.ui.common.NoContentsPlaceHolder
import me.sanao1006.screens.AnnouncementScreen
import me.sanao1006.screens.event.globalIcon.GlobalIconEvent
import me.snao1006.res_value.ResString

@Composable
@CircuitInject(AnnouncementScreen::class, SingletonComponent::class)
fun AnnouncementScreen(state: AnnouncementScreen.State, modifier: Modifier) {
    Box(modifier = modifier.fillMaxSize()) {
        val snackbarHostState = remember { SnackbarHostState() }
        DrawerItemScreenWrapper(
            drawerItem = DrawerItem.ANNOUNCEMENT,
            snackbarHostState = snackbarHostState,
            onBackIconClick = { state.globalIconEventSink(GlobalIconEvent.OnBackBeforeScreen) }
        ) {
            Column(modifier = it.fillMaxSize()) {
                when (state.uiState) {
                    is AnnouncementUiState.Loading -> {
                        ContentLoadingIndicator()
                    }

                    is AnnouncementUiState.Failed -> {
                        NoContentsPlaceHolder()
                    }

                    is AnnouncementUiState.Success -> {
                        val announcements =
                            (state.uiState as AnnouncementUiState.Success).announcements
                        AnnouncementScreenUiContent(
                            selectedTabIndex = state.selectedTabIndex,
                            announcements = announcements,
                            announcementItemExpandedStates = state.announcementItemExpandedStates,
                            modifier = Modifier.fillMaxSize(),
                            onCurrentTabClick = {
                                state.eventSink(
                                    AnnouncementScreen.Event.OnTabClicked(
                                        0
                                    )
                                )
                            },
                            onPastTabClick = {
                                state.eventSink(
                                    AnnouncementScreen.Event.OnTabClicked(
                                        1
                                    )
                                )
                            },
                            onCardClick = {
                                state.eventSink(
                                    AnnouncementScreen.Event.OnCardClicked(
                                        it
                                    )
                                )
                            }
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
    selectedTabIndex: Int,
    announcementItemExpandedStates: Map<String, Boolean>,
    modifier: Modifier = Modifier,
    onCurrentTabClick: () -> Unit,
    onPastTabClick: () -> Unit,
    onCardClick: (String) -> Unit
) {
    Column(modifier = modifier.fillMaxWidth()) {
        TabRow(selectedTabIndex = selectedTabIndex) {
            Tab(
                selected = selectedTabIndex == 0,
                onClick = onCurrentTabClick,
                text = { Text(stringResource(ResString.announcement_current_announcements)) }
            )
            Tab(
                selected = selectedTabIndex == 1,
                onClick = onPastTabClick,
                text = { Text(stringResource(ResString.announcement_past_announcements)) }
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        if (announcements.isEmpty()) {
            NoContentsPlaceHolder()
        } else {
            AnnouncementsView(
                announcements = announcements,
                expandedStates = announcementItemExpandedStates,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                onClick = onCardClick
            )
        }
    }
}

@Composable
private fun AnnouncementsView(
    announcements: List<Announcement>,
    expandedStates: Map<String, Boolean>,
    modifier: Modifier = Modifier,
    onClick: (String) -> Unit
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        items(
            items = announcements,
            key = { it.id }
        ) { announcement ->
            val isExpanded = expandedStates[announcement.id] == true

            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                onClick = { onClick(announcement.id) }
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

@PreviewLightDark
@Composable
private fun PreviewAnnouncementScreenUiContent() {
    MintTheme {
        val announcements = listOf<Announcement>(
            Announcement(
                id = "1",
                title = "Title 1",
                icon = "info",
                imageUrl = null,
                text = "Text 1",
                createdAt = "2022-01-01T00:00:00Z",
                updatedAt = null,
                display = "test",
                forYou = true,
                isRead = false,
                needConfirmationToRead = true,
                silence = true
            ),
            Announcement(
                id = "2",
                title = "Test Title 1",
                icon = "info",
                imageUrl = null,
                text = """
                    | Test Text2 Test Text2Test
                    | Text2Test
                    | Text2Test Text2Test
                    | Text2Test
                    | Text2Test Text2Test
                    | Text2Test Text2
                """.trimMargin(),
                createdAt = "2022-01-01T00:00:00Z",
                updatedAt = null,
                display = "test",
                forYou = true,
                isRead = false,
                needConfirmationToRead = true,
                silence = true
            )
        )
        AnnouncementScreenUiContent(
            selectedTabIndex = 0,
            announcements = announcements,
            announcementItemExpandedStates = mapOf(),
            onCurrentTabClick = {},
            onPastTabClick = {},
            onCardClick = {}
        )
    }
}
