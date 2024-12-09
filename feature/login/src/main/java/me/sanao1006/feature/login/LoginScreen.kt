package me.sanao1006.feature.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.slack.circuit.codegen.annotations.CircuitInject
import dagger.hilt.components.SingletonComponent
import me.sanao1006.screens.AuthStateType
import me.sanao1006.screens.LoginScreen
import me.snao1006.res_value.ResString

@CircuitInject(LoginScreen::class, SingletonComponent::class)
@Composable
fun LoginScreenUi(state: LoginScreen.State, modifier: Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(vertical = 64.dp, horizontal = 24.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        LoginContent(state = state)
    }
}

@Composable
private fun LoginContent(state: LoginScreen.State) {
    Column(
        modifier = Modifier.padding(64.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        val scope = rememberCoroutineScope()
        val context = LocalContext.current
        Text(
            text = stringResource(ResString.enter_domain),
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.padding(8.dp))
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
            modifier = Modifier.fillMaxWidth(),
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
                Text(stringResource(ResString.login_re_authentication))
            } else {
                Text(stringResource(ResString.login_authentication))
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
                    .fillMaxWidth(),
                onClick = { state.eventSink(LoginScreen.Event.OnAuthButtonClicked(scope, context)) }
            ) {
                Text(stringResource(ResString.login))
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
