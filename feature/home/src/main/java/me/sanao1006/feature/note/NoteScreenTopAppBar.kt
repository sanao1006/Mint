package me.sanao1006.feature.note

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import compose.icons.TablerIcons
import compose.icons.tablericons.ArrowLeft

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteScreenTopAppBar(
    modifier: Modifier = Modifier,
    onBackClicked: () -> Unit,
    onNotePostClicked: () -> Unit
) {
    TopAppBar(
        modifier = modifier,
        navigationIcon = {
            IconButton(onClick = onBackClicked) {
                Icon(
                    imageVector = TablerIcons.ArrowLeft,
                    contentDescription = "Back"
                )
            }
        },
        actions = {
            FilledTonalButton(
                modifier = Modifier.padding(horizontal = 8.dp),
                onClick = onNotePostClicked
            ) {
                Text("Post")
            }
        },
        title = {}
    )
}
