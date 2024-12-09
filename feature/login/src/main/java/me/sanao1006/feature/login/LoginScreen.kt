package me.sanao1006.feature.login

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.slack.circuit.codegen.annotations.CircuitInject
import dagger.hilt.components.SingletonComponent
import me.sanao1006.screens.AuthStateType
import me.sanao1006.screens.LoginScreen
import me.snao1006.res_value.ResDrawable
import me.snao1006.res_value.ResString

@CircuitInject(LoginScreen::class, SingletonComponent::class)
@Composable
fun LoginScreenUi(state: LoginScreen.State, modifier: Modifier) {
    var showIcon by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        showIcon = true
    }
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        AnimatedVisibility(
            visible = showIcon,
            enter = fadeIn(animationSpec = tween(durationMillis = 1500))
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Image(
                    modifier = Modifier.size(128.dp),
                    painter = painterResource(ResDrawable.ic_main_icon),
                    contentDescription = ""
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Welcome to Mint!",
                    style = MaterialTheme.typography.displaySmall,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }
        LoginContent(
            state = state,
            modifier = Modifier.padding(vertical = 32.dp, horizontal = 56.dp)
        )
    }
}

@Composable
private fun LoginContent(state: LoginScreen.State, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        val scope = rememberCoroutineScope()
        val context = LocalContext.current
        Text(
            text = stringResource(ResString.enter_domain),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.secondary
        )
        Spacer(modifier = Modifier.padding(6.dp))
        TextField(
            value = state.domain,
            maxLines = 1,
            placeholder = {
                Text(
                    text = "https://misskey.io",
                    color = MaterialTheme.colorScheme.outline
                )
            },
            onValueChange = { state.eventSink(LoginScreen.Event.OnTextChanged(it)) }
        )
        Spacer(modifier = Modifier.padding(8.dp))
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            enabled = state.buttonEnabled,
            onClick = {
                state.eventSink(
                    LoginScreen.Event.OnButtonClicked(
                        scope = scope,
                        context = context
                    )
                )
            }
        ) {
            if (state.authState == AuthStateType.WAITING) {
                Text(
                    text = stringResource(ResString.login_re_authentication),
                    color = Color.White
                )
            } else {
                Text(
                    text = stringResource(ResString.login_authentication),
                    color = Color.White
                )
            }
        }
        if (state.authState == AuthStateType.WAITING) {
            Spacer(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
            )
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                onClick = { state.eventSink(LoginScreen.Event.OnAuthButtonClicked(scope, context)) }
            ) {
                Text(
                    text = stringResource(ResString.login),
                    color = Color.White
                )
            }
        }
    }
}

@PreviewLightDark
@Composable
fun PreviewLoginScreenUi() {
    LoginScreenUi(
        state = LoginScreen.State(
            domain = "example.com",
            eventSink = {}
        ),
        modifier = Modifier
    )
}
