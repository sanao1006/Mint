package me.sanao1006.feature.login

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.presenter.Presenter
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.launch
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
        val scope = rememberCoroutineScope()
        return LoginScreen.State(
            domain = domain
        ) { event ->
            when (event) {
                is LoginScreen.Event.OnTextChanged -> {
                    domain = event.text
                }

                is LoginScreen.Event.OnButtonClicked -> {
                    scope.launch {
                        val session = Uuid.random().toString()
                        openUrlInChrome(
                            url = "${domain}/miauth/$session?name=Mint&permission=read:account,write:account&callback=myapp://auth-callback",
                            context = event.context
                        )
                    }
                }
            }
        }
    }
}

private fun openUrlInChrome(url: String, context: Context) {
    // URLのバリデーションチェック
    if (url.isBlank() || !android.util.Patterns.WEB_URL.matcher(url).matches()) {
        Toast.makeText(context, "Invalid Url", Toast.LENGTH_SHORT).show()
        return
    }

    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
        setPackage("com.android.chrome")
    }

    try {
        // Check if the Chrome browser is installed
        if (intent.resolveActivity(context.packageManager) != null) {
            context.startActivity(intent)
        } else {
            // Chrome browser is not installed, open in other browser
            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
        }
    } catch (e: ActivityNotFoundException) {
        Toast.makeText(context, "Browser not found", Toast.LENGTH_SHORT).show()
    } catch (e: Exception) {
        Toast.makeText(context, "Failed to open the url", Toast.LENGTH_SHORT).show()
    }
}

enum class AuthStateType { FIXED, WAITING, SUCCESS }
