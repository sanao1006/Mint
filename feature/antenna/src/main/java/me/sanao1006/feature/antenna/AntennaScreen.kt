package me.sanao1006.feature.antenna

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.slack.circuit.codegen.annotations.CircuitInject
import dagger.hilt.components.SingletonComponent
import ir.alirezaivaz.tablericons.TablerIcons
import me.sanao1006.core.model.antenna.AntennaResponse
import me.sanao1006.core.model.uistate.AntennaScreenUiState
import me.sanao1006.core.ui.DrawerItem
import me.sanao1006.core.ui.DrawerItemScreenWrapper
import me.sanao1006.core.ui.common.ContentLoadingIndicator
import me.sanao1006.core.ui.common.NoContentsPlaceHolder
import me.sanao1006.screens.AntennaScreen

@Composable
@CircuitInject(AntennaScreen::class, SingletonComponent::class)
fun AntennaScreen(state: AntennaScreen.State, modifier: Modifier) {
    Box(modifier = modifier.fillMaxSize()) {
        DrawerItemScreenWrapper(
            drawerItem = DrawerItem.ANTENNA,
            globalIconEventSink = state.globalIconEventSink
        ) {
            Column(modifier = it.fillMaxSize()) {
                when (state.uiState) {
                    is AntennaScreenUiState.Loading -> {
                        ContentLoadingIndicator()
                    }

                    is AntennaScreenUiState.Failure -> {
                        NoContentsPlaceHolder()
                    }

                    is AntennaScreenUiState.Success -> {
                        val antennaList =
                            (state.uiState as AntennaScreenUiState.Success).antennaList
                        if (antennaList.isEmpty()) {
                            NoContentsPlaceHolder()
                        } else {
                            AntennaScreenUiContent(
                                antennaList = antennaList,
                                modifier = Modifier.fillMaxSize(),
                                onCardClick = { _ -> },
                                onEditClick = { },
                                onDeleteClick = { }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AntennaScreenUiContent(
    antennaList: List<AntennaResponse>,
    modifier: Modifier = Modifier,
    onCardClick: (String) -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Column(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(antennaList) { item ->
                AntennaCard(
                    antenna = item,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp),
                    onCardClick = onCardClick,
                    onEditClick = onEditClick,
                    onDeleteClick = onDeleteClick
                )
            }
        }
    }
}

@Composable
private fun AntennaCard(
    antenna: AntennaResponse,
    modifier: Modifier = Modifier,
    onCardClick: (String) -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    ElevatedCard(
        onClick = { onCardClick(antenna.name) },
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        AntennaItem(
            antennaName = antenna.name,
            modifier = Modifier.padding(16.dp),
            onEditClick = onEditClick,
            onDeleteClick = onDeleteClick
        )
    }
}

@Composable
private fun AntennaItem(
    antennaName: String,
    modifier: Modifier = Modifier,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyMedium,
            text = antennaName,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis
        )
        Row {
            Icon(
                modifier = Modifier.clickable { onEditClick() },
                painter = painterResource(TablerIcons.Edit),
                contentDescription = ""
            )
            Spacer(modifier = Modifier.width(16.dp))
            Icon(
                modifier = Modifier.clickable { onDeleteClick() },
                painter = painterResource(TablerIcons.Trash),
                contentDescription = ""
            )
        }
    }
}
