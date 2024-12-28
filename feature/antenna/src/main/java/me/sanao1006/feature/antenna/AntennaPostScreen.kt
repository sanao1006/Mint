package me.sanao1006.feature.antenna

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.slack.circuit.codegen.annotations.CircuitInject
import dagger.hilt.components.SingletonComponent
import ir.alirezaivaz.tablericons.TablerIcons
import me.sanao1006.core.ui.DrawerItem
import me.sanao1006.core.ui.DrawerItemScreenWrapper
import me.sanao1006.screens.AntennaPostScreen
import me.sanao1006.screens.AntennaSource
import me.sanao1006.screens.event.globalIcon.GlobalIconEvent
import me.snao1006.res_value.ResString

@CircuitInject(AntennaPostScreen::class, SingletonComponent::class)
@Composable
fun AntennaPostScreen(state: AntennaPostScreen.State, modifier: Modifier) {
    Box(modifier = modifier.fillMaxSize()) {
        val snackbarHostState = remember { SnackbarHostState() }
        DrawerItemScreenWrapper(
            drawerItem = DrawerItem.ANTENNA,
            snackbarHostState = snackbarHostState,
            onBackIconClick = {
                state.globalIconEventSink(GlobalIconEvent.OnBackBeforeScreen)
            }
        ) {
            AntennaPostScreenUiContent(
                state = state,
                snackbarHostState = snackbarHostState,
                modifier = it
                    .fillMaxWidth()
                    .padding(vertical = 16.dp, horizontal = 24.dp)
                    .verticalScroll(rememberScrollState())
            )
        }
    }
}

@Composable
private fun AntennaPostScreenUiContent(
    state: AntennaPostScreen.State,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        AntennaPostForm(
            antennaName = state.uiState.antennaName,
            expanded = state.uiState.expanded,
            isBotAccountExcluded = state.uiState.isBotAccountExcluded,
            antennaSource = state.uiState.antennaSource,
            username = state.uiState.users?.joinToString("\n") ?: "",
            isReplyIncluded = state.uiState.isReplyIncluded,
            keywordValue = state.uiState.keywordValue,
            exceptedKeywordValue = state.uiState.exceptedKeywordValue,
            isLocalOnly = state.uiState.isLocalOnly,
            isCaseSensitive = state.uiState.isCaseSensitive,
            isOnlyFileNote = state.uiState.isOnlyFileNote,
            onAntennaChange = {
                state.eventSink(AntennaPostScreen.Event.OnAntennaNameChange(it))
            },
            onExpandedChange = { state.eventSink(AntennaPostScreen.Event.OnExpandClick(it)) },
            onDropDownItemClick = {
                state.eventSink(AntennaPostScreen.Event.OnDropDownItemClick(it))
            },
            onUsernameChange = { text, antennaSource ->
                state.eventSink(
                    AntennaPostScreen.Event.OnUsersNameChange(
                        text,
                        antennaSource
                    )
                )
            },
            onBotAccountExcludedChange = {
                state.eventSink(
                    AntennaPostScreen.Event.OnBotAccountExcludedChange(
                        it
                    )
                )
            },
            onReplyIncludedChange = {
                state.eventSink(
                    AntennaPostScreen.Event.OnReplyIncludedChange(
                        it
                    )
                )
            },
            onKeywordChange = {
                state.eventSink(AntennaPostScreen.Event.OnKeywordValueChange(it))
            },
            onExceptedKeywordChange = {
                state.eventSink(
                    AntennaPostScreen.Event.OnExceptedKeywordValueChange(
                        it
                    )
                )
            },
            onLocalOnlyChange = {
                state.eventSink(AntennaPostScreen.Event.OnLocalOnlyChange(it))
            },
            onCaseSensitiveChange = {
                state.eventSink(
                    AntennaPostScreen.Event.OnCaseSensitiveChange(
                        it
                    )
                )
            },
            onOnlyFileNoteChange = {
                state.eventSink(AntennaPostScreen.Event.OnOnlyFileNoteChange(it))
            }
        )
        Spacer(modifier = Modifier.height(32.dp))
        AntennaPostButtons(
            isEdit = state.isEdit,
            onSaveButtonClick = {
                state.eventSink(AntennaPostScreen.Event.OnSaveClick(snackbarHostState))
            },
            onDeleteButtonClick = {
                state.eventSink(AntennaPostScreen.Event.OnDeleteClick(snackbarHostState))
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AntennaPostForm(
    antennaName: String,
    expanded: Boolean,
    isBotAccountExcluded: Boolean,
    antennaSource: String,
    username: String,
    isReplyIncluded: Boolean,
    keywordValue: String,
    exceptedKeywordValue: String,
    isLocalOnly: Boolean,
    isCaseSensitive: Boolean,
    isOnlyFileNote: Boolean,
    modifier: Modifier = Modifier,
    onAntennaChange: (String) -> Unit,
    onExpandedChange: (Boolean) -> Unit,
    onDropDownItemClick: (String) -> Unit,
    onBotAccountExcludedChange: (Boolean) -> Unit,
    onUsernameChange: (String, String) -> Unit,
    onReplyIncludedChange: (Boolean) -> Unit,
    onKeywordChange: (String) -> Unit,
    onExceptedKeywordChange: (String) -> Unit,
    onLocalOnlyChange: (Boolean) -> Unit,
    onCaseSensitiveChange: (Boolean) -> Unit,
    onOnlyFileNoteChange: (Boolean) -> Unit
) {
    Column(modifier = modifier) {
        val antennaSourceName = AntennaSource.fromBodyToName(antennaSource)
        Text(
            text = stringResource(ResString.antenna_name)
        )
        TextField(
            value = antennaName,
            onValueChange = onAntennaChange
        )
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(ResString.antenna_source)
        )
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = onExpandedChange
        ) {
            TextField(
                value = antennaSourceName,
                modifier = Modifier.menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable),
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                colors = ExposedDropdownMenuDefaults.textFieldColors()
            )
            DropdownMenu(
                modifier = Modifier.fillMaxWidth(0.8f),
                expanded = expanded,
                onDismissRequest = { onExpandedChange(false) }
            ) {
                AntennaSource.entries.forEach {
                    DropdownMenuItem(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            onDropDownItemClick(it.body)
                        },
                        text = { Text(stringResource(it.resId)) }
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        if (antennaSource == AntennaSource.USERS.body ||
            antennaSource == AntennaSource.USERS_BLACKLIST.body
        ) {
            Text(text = stringResource(ResString.antenna_users))
            TextField(
                value = username,
                onValueChange = { text ->
                    onUsernameChange(text, antennaSource)
                }
            )
        }

        Spacer(modifier = Modifier.height(8.dp))
        AntennaPostSwitch(
            label = stringResource(ResString.antenna_exclude_bot_accounts),
            checked = isBotAccountExcluded,
            onCheckedChange = onBotAccountExcludedChange
        )
        Spacer(modifier = Modifier.height(8.dp))
        AntennaPostSwitch(
            label = stringResource(ResString.antenna_include_replies),
            checked = isReplyIncluded,
            onCheckedChange = onReplyIncludedChange
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = stringResource(ResString.antenna_keywords))
        TextField(
            value = keywordValue,
            onValueChange = onKeywordChange
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = stringResource(ResString.antenna_keywords_exclude))
        TextField(
            value = exceptedKeywordValue,
            onValueChange = onExceptedKeywordChange
        )

        AntennaPostSwitch(
            label = stringResource(ResString.antenna_local_only),
            checked = isLocalOnly,
            onCheckedChange = onLocalOnlyChange
        )
        Spacer(modifier = Modifier.height(8.dp))
        AntennaPostSwitch(
            label = stringResource(ResString.antenna_case_sensitive),
            checked = isCaseSensitive,
            onCheckedChange = onCaseSensitiveChange
        )
        Spacer(modifier = Modifier.height(8.dp))
        AntennaPostSwitch(
            label = stringResource(ResString.antenna_only_files),
            checked = isOnlyFileNote,
            onCheckedChange = onOnlyFileNoteChange
        )
    }
}

@Composable
private fun AntennaPostButtons(
    isEdit: Boolean,
    modifier: Modifier = Modifier,
    onSaveButtonClick: () -> Unit,
    onDeleteButtonClick: () -> Unit
) {
    Row(modifier = modifier) {
        Button(
            onClick = onSaveButtonClick
        ) {
            Icon(
                painter = painterResource(TablerIcons.DeviceFloppy),
                ""
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = stringResource(ResString.save_description))
        }
        if (isEdit) {
            Spacer(modifier = Modifier.width(16.dp))
            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                ),
                onClick = onDeleteButtonClick
            ) {
                Icon(
                    painter = painterResource(TablerIcons.Trash),
                    ""
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = stringResource(ResString.delete_description))
            }
        }
    }
}

@Composable
private fun AntennaPostSwitch(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterVertically)
        )
        Switch(
            checked = checked,
            thumbContent = {
                Icon(
                    painter = painterResource(TablerIcons.Check),
                    ""
                )
            },
            onCheckedChange = onCheckedChange
        )
    }
}
