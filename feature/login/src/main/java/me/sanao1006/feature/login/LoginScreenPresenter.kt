package me.sanao1006.feature.login

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.presenter.Presenter
import dagger.hilt.components.SingletonComponent
import me.sanao1006.core.network.api.MiauthRepository
import javax.inject.Inject
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@CircuitInject(LoginScreen::class, SingletonComponent::class)
class LoginScreenPresenter @Inject constructor(
    private val miauthRepository: MiauthRepository
) : Presenter<LoginScreen.State> {
    @OptIn(ExperimentalUuidApi::class)
    @Composable
    override fun present(): LoginScreen.State {
        var domain by rememberRetained { mutableStateOf("") }
        var authState by rememberRetained { mutableStateOf(AuthStateType.FIXED) }
        return LoginScreen.State(
            domain = domain
        ) { event ->
            when (event) {
                is LoginScreen.Event.OnTextChanged -> {
                    domain = event.text
                }

                is LoginScreen.Event.OnButtonClicked -> {
                    val session = Uuid.random().toString()
                    openUrlInChrome(
                        url = "${domain}/miauth/$session?name=Mint&permission=read:account,write:account",
                        context = event.context
                    )
                    authState = AuthStateType.WAITING
                }
            }
        }
    }
}


private fun openUrlInChrome(url: String, context: Context) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
        setPackage("com.android.chrome")
    }

    if (intent.resolveActivity(context.packageManager) != null) {
        context.startActivity(intent)
    } else {
        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    }
}

enum class AuthStateType { FIXED, WAITING, SUCCESS }
