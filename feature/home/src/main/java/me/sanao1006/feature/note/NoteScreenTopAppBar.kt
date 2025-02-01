package me.sanao1006.feature.note

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ir.alirezaivaz.tablericons.TablerIcons
import me.sanao1006.core.model.notes.ReactionAcceptance
import me.sanao1006.core.model.notes.Visibility
import me.sanao1006.core.model.uistate.NoteOptionContent
import me.snao1006.res_value.ResString

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteScreenTopAppBar(
    isSubmitEnabled: Boolean,
    isShowBottomSheet: Boolean,
    noteOptionContent: NoteOptionContent,
    noteOptionState: NoteOptionState,
    modifier: Modifier = Modifier,
    onBottomSheetOuterClicked: () -> Unit,
    onIconClicked: (NoteOptionContent) -> Unit,
    onVisibilityClicked: (Visibility) -> Unit,
    onLocalOnlyClicked: (Boolean) -> Unit,
    onReactionAcceptanceClicked: (ReactionAcceptance?) -> Unit,
    onBackClicked: () -> Unit,
    onNotePostClicked: () -> Unit
) {
    TopAppBar(
        modifier = modifier,
        navigationIcon = {
            IconButton(onClick = onBackClicked) {
                Icon(
                    painter = painterResource(TablerIcons.ArrowLeft),
                    contentDescription = "Back"
                )
            }
        },
        actions = {
            Row {
                NoteOptionRow(
                    isShowBottomSheet = isShowBottomSheet,
                    noteOptionContent = noteOptionContent,
                    noteOptionState = noteOptionState,

                    onBottomSheetOuterClicked = onBottomSheetOuterClicked,
                    onIconClicked = onIconClicked,
                    onVisibilityClicked = onVisibilityClicked,
                    onLocalOnlyClicked = onLocalOnlyClicked,
                    onReactionAcceptanceClicked = onReactionAcceptanceClicked
                )
                FilledTonalButton(
                    enabled = isSubmitEnabled,
                    modifier = Modifier.padding(horizontal = 8.dp),
                    onClick = onNotePostClicked
                ) {
                    Text(stringResource(ResString.create_post))
                }
            }
        },
        title = {}
    )
}
