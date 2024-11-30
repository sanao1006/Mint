package me.sanao1006.feature.note

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import ir.alirezaivaz.tablericons.TablerIcons

@Composable
internal fun NoteOptionRow(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = {}) {
            Icon(painter = painterResource(TablerIcons.World), "")
        }
        Spacer(modifier = Modifier.width(4.dp))
        IconButton(onClick = {}) {
            Icon(painter = painterResource(TablerIcons.Rocket), "")
        }
        Spacer(modifier = Modifier.width(4.dp))
        IconButton(onClick = {}) {
            Icon(painter = painterResource(TablerIcons.Icons), "")
        }
        Spacer(modifier = Modifier.width(8.dp))
    }
}