package me.sanao1006.feature.user

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil3.ImageLoader
import coil3.compose.AsyncImage
import coil3.compose.rememberAsyncImagePainter
import coil3.gif.GifDecoder
import com.slack.circuit.codegen.annotations.CircuitInject
import dagger.hilt.components.SingletonComponent
import ir.alirezaivaz.tablericons.TablerIcons
import me.sanao1006.screens.UserScreen

@OptIn(ExperimentalMaterial3Api::class)
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
                            onClick = { state.eventSink(UserScreen.Event.OnNavigationIconClicked) }
                        ) {
                            Icon(painter = painterResource(TablerIcons.ArrowLeft), "")
                        }
                    }
                )
            }
        ) {
            Column(
                modifier = Modifier.padding(it)
            ) {
                HeaderContent(
                    bannerUrl = state.uiState.bannerUrl,
                    avatarUrl = state.uiState.avatarUrl
                )
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
    Column(modifier = modifier) {
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
                .align(Alignment.CenterHorizontally)
                .offset(y = (-50).dp)
        ) {
            Image(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape),
                painter = rememberAsyncImagePainter(avatarUrl),
                contentDescription = "",
            )
        }
    }
}