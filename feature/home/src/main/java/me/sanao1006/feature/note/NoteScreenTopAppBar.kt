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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ir.alirezaivaz.tablericons.TablerIcons
import me.snao1006.res_value.ResString

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
                    painter = painterResource(TablerIcons.ArrowLeft),
                    contentDescription = "Back"
                )
            }
        },
        actions = {
            FilledTonalButton(
                modifier = Modifier.padding(horizontal = 8.dp),
                onClick = onNotePostClicked
            ) {
                Text(stringResource(ResString.create_post))
            }
        },
        title = {}
    )
}
