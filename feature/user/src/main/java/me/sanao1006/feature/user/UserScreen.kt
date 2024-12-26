package me.sanao1006.feature.user

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil3.ImageLoader
import coil3.compose.AsyncImage
import coil3.compose.rememberAsyncImagePainter
import coil3.gif.GifDecoder
import com.slack.circuit.codegen.annotations.CircuitInject
import dagger.hilt.components.SingletonComponent
import ir.alirezaivaz.tablericons.TablerIcons
import me.sanao1006.core.model.common.Field
import me.sanao1006.core.model.uistate.UserScreenUiState
import me.sanao1006.core.ui.common.ContentLoadingIndicator
import me.sanao1006.screens.UserScreen
import me.sanao1006.screens.event.globalIcon.GlobalIconEvent
import me.snao1006.res_value.ResString

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@CircuitInject(UserScreen::class, SingletonComponent::class)
@Composable
fun UserScreenUi(state: UserScreen.State, modifier: Modifier) {
    Box(modifier = modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { },
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                state.globalIconEventSink(GlobalIconEvent.OnArrowBackIconClicked)
                            }
                        ) {
                            Icon(painter = painterResource(TablerIcons.ArrowLeft), "")
                        }
                    }
                )
            }
        ) {
            when (val uiState = state.uiState) {
                is UserScreenUiState.Loading -> {
                    ContentLoadingIndicator()
                }

                is UserScreenUiState.Failed -> {
                    state.eventSink(UserScreen.Event.OnLoadingFailed)
                }

                is UserScreenUiState.Success -> {
                    Column(
                        modifier = Modifier
                            .padding(it)
                            .verticalScroll(rememberScrollState())
                    ) {
                        HeaderContent(
                            bannerUrl = uiState.bannerUrl,
                            avatarUrl = uiState.avatarUrl
                        )
                        Spacer(modifier = Modifier.height(60.dp))
                        UserNameContent(
                            username = uiState.username,
                            name = uiState.name,
                            host = uiState.host
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        BioContent(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            description = uiState.description,
                            fields = uiState.fields
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        PostInfoContent(
                            notesCount = uiState.notesCount,
                            followingCount = uiState.followingCount,
                            followersCount = uiState.followersCount,
                            modifier = Modifier
                                .fillMaxWidth(0.8f)
                                .align(Alignment.CenterHorizontally)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun HeaderContent(
    bannerUrl: String?,
    avatarUrl: String?,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        if (bannerUrl.isNullOrEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(Color.Gray)
            )
        } else {
            AsyncImage(
                modifier = Modifier
                    .height(200.dp)
                    .fillMaxWidth(),
                model = if (bannerUrl!!.endsWith(".gif")) {
                    rememberAsyncImagePainter(
                        model = bannerUrl,
                        imageLoader = ImageLoader
                            .Builder(LocalContext.current)
                            .components {
                                add(GifDecoder.Factory())
                            }
                            .build()
                    )
                } else {
                    bannerUrl
                },
                contentDescription = "",
                contentScale = ContentScale.Crop
            )
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .offset(y = (50).dp)
        ) {
            Image(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape),
                painter = rememberAsyncImagePainter(avatarUrl),
                contentDescription = "",
                contentScale = ContentScale.Crop
            )
        }
    }
}

@Composable
private fun ColumnScope.UserNameContent(
    username: String,
    name: String?,
    host: String?,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.align(Alignment.CenterHorizontally),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = name ?: username,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = username + (host?.let { "@$it" } ?: ""),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun BioContent(
    description: String?,
    fields: List<Field>?,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = description ?: "",
            textAlign = TextAlign.Center
        )
        if (!fields.isNullOrEmpty()) {
            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))
            fields.forEachIndexed { index, it ->
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = it.name,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.width(120.dp),
                        textAlign = TextAlign.Start
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = it.value,
                        modifier = Modifier
                            .weight(1f)
                            .alignByBaseline()
                    )
                }
                if (index != fields.size - 1) {
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
private fun PostInfoContent(
    notesCount: Int,
    followingCount: Int,
    followersCount: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(text = stringResource(ResString.notes_count))
            Text(notesCount.toString())
        }
        Column {
            Text(text = stringResource(ResString.drawer_following))
            Text(followingCount.toString())
        }
        Column {
            Text(text = stringResource(ResString.drawer_followers))
            Text(followersCount.toString())
        }
    }
}
