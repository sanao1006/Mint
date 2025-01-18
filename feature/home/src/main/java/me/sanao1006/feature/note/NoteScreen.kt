package me.sanao1006.feature.note

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.retained.rememberRetained
import com.slack.circuitx.effects.LaunchedImpressionEffect
import dagger.hilt.components.SingletonComponent
import ir.alirezaivaz.tablericons.TablerIcons
import me.sanao1006.core.designsystem.MintTheme
import me.sanao1006.core.model.uistate.NoteOptionContent
import me.sanao1006.core.model.uistate.NoteTargetState
import me.sanao1006.screens.NoteScreen
import me.snao1006.res_value.ResString

@CircuitInject(NoteScreen::class, SingletonComponent::class)
@Composable
fun NoteScreenUi(state: NoteScreen.State, modifier: Modifier) {
    MintTheme {
        val scope = rememberCoroutineScope()
        val focusManager = LocalFocusManager.current
        val focusRequester = rememberRetained { FocusRequester() }
        LaunchedImpressionEffect(Unit) {
            focusRequester.requestFocus()
        }
        Box(
            modifier = modifier.fillMaxSize()
        ) {
            Scaffold(
                topBar = {
                    NoteScreenTopAppBar(
                        isSubmitEnabled = state.uiState.noteText.isNotBlank() &&
                            (state.uiState.expandCw && !state.uiState.cw.isNullOrBlank()),
                        isShowBottomSheet = state.uiState.isShowBottomSheet,
                        noteOptionContent = state.uiState.noteOptionContent,
                        noteOptionState = NoteOptionState(
                            visibility = state.uiState.visibility,
                            localOnly = state.uiState.localOnly,
                            reactionAcceptance = state.uiState.reactionAcceptance
                        ),
                        onBottomSheetOuterClicked = {
                            state.eventSink(NoteScreen.Event.OnHideBottomSheet)
                        },
                        onIconClicked = {
                            state.eventSink(NoteScreen.Event.OnShowBottomSheet(it))
                        },
                        onVisibilityClicked = {
                            state.eventSink(NoteScreen.Event.OnVisibilityChanged(it))
                        },
                        onLocalOnlyClicked = {
                            state.eventSink(NoteScreen.Event.OnLocalOnlyChanged(it))
                        },
                        onReactionAcceptanceClicked = {
                            state.eventSink(NoteScreen.Event.OnReactionAcceptanceChanged(it))
                        },
                        onBackClicked = { state.eventSink(NoteScreen.Event.OnBackClicked) },
                        onNotePostClicked = {
                            focusManager.clearFocus()
                            state.eventSink(
                                NoteScreen.Event.OnNotePostClicked(
                                    scope
                                )
                            )
                        }
                    )
                }
            ) {
                NoteScreenContent(
                    modifier = Modifier
                        .padding(it)
                        .focusRequester(focusRequester),
                    state = state
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NoteScreenContent(
    state: NoteScreen.State,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        // reply or quote target
        state.uiState.noteTarget?.let {
            ReplyTargetNote(it)
        }

        NoteScreenTextField(
            expandCw = state.uiState.expandCw,
            text = state.uiState.noteText,
            cwText = state.uiState.cw,
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            onCwValueChange = { state.eventSink(NoteScreen.Event.OnCwTextChanged(it)) },
            onCwClick = { state.eventSink(NoteScreen.Event.OnCwEnabledChanged) }
        ) { note ->
            state.eventSink(NoteScreen.Event.OnNoteTextChanged(note))
        }
    }
}

@Composable
private fun NoteScreenTextField(
    text: String,
    cwText: String?,
    expandCw: Boolean,
    modifier: Modifier = Modifier,
    onCwClick: () -> Unit,
    onCwValueChange: (String) -> Unit,
    onValueChange: (String) -> Unit
) {
    if (expandCw) {
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = cwText ?: "",
            placeholder = {
                Text(stringResource(ResString.cw_comments))
            },
            maxLines = 1,
            onValueChange = onCwValueChange
        )
    }
    TextField(
        modifier = modifier,
        value = text,
        onValueChange = onValueChange,
        placeholder = {
            Text(
                text = stringResource(ResString.note_placeholder),
                style = MaterialTheme.typography.bodyLarge
            )
        }
    )
    NoteActionRow(
        expandCw = expandCw,
        modifier = Modifier
            .imePadding()
            .fillMaxWidth()
            .padding(horizontal = 4.dp),
        onCwClick = onCwClick
    )
}

@Composable
private fun NoteActionRow(
    expandCw: Boolean,
    modifier: Modifier = Modifier,
    onCwClick: () -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onCwClick) {
            Icon(
                painter = painterResource(TablerIcons.EyeOff),
                contentDescription = "",
                tint = if (expandCw) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.onSurface
                }
            )
        }
    }
}

@Composable
private fun ReplyTargetNote(noteTargetState: NoteTargetState) {
    ElevatedCard {
        ListItem(
            leadingContent = {
                AsyncImage(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(shape = CircleShape),
                    model = noteTargetState.avatarUrl,
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )
            },
            headlineContent = {
                Text(
                    text = noteTargetState.name ?: noteTargetState.userName,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            },
            supportingContent = {
                Text(
                    text = noteTargetState.text,
                    maxLines = 5,
                    overflow = TextOverflow.Ellipsis
                )
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@PreviewLightDark
@Composable
fun A() {
    MintTheme {
        NoteOptionRow(
            true,
            NoteOptionContent.VISIBILITY,
            NoteOptionState(
                me.sanao1006.core.model.notes.Visibility.PUBLIC,
                false,
                me.sanao1006.core.model.notes.ReactionAcceptance.LIKE_ONLY
            ),
            Modifier,
            {},
            {},
            {},
            {},
            {}
        )
    }
}
