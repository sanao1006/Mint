package me.sanao1006.feature.login

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.presenter.Presenter
import dagger.hilt.components.SingletonComponent
import de.jensklingenberg.ktorfit.Ktorfit
import de.jensklingenberg.ktorfit.converter.FlowConverterFactory
import de.jensklingenberg.ktorfit.converter.ResponseConverterFactory
import io.ktor.client.HttpClient
import kotlinx.coroutines.launch
import me.sanao1006.core.model.AppCreateRequestBody
import me.sanao1006.core.network.api.MiauthRepository
import me.sanao1006.core.network.api.createMiauthRepository
import javax.inject.Inject
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@CircuitInject(LoginScreen::class, SingletonComponent::class)
class LoginScreenPresenter @Inject constructor(
    private val miauthRepository: MiauthRepository,
    private val httpClient: HttpClient
) : Presenter<LoginScreen.State> {
    @OptIn(ExperimentalUuidApi::class)
    @Composable
    override fun present(): LoginScreen.State {
        var domain by rememberRetained { mutableStateOf("") }
        val session by rememberRetained { mutableStateOf(Uuid.random().toString()) }
        var authState by rememberRetained { mutableStateOf(AuthStateType.FIXED) }
        val ktorfit = Ktorfit.Builder()
        return LoginScreen.State(
            domain = domain,
            authState = authState
        ) { event ->
            when (event) {
                is LoginScreen.Event.OnTextChanged -> {
                    domain = event.text
                }

                is LoginScreen.Event.OnButtonClicked -> {
                    val ktorfitClient =
                        ktorfit.httpClient(httpClient).baseUrl("${domain}/")
                            .converterFactories(
                                FlowConverterFactory(),
                                ResponseConverterFactory()
                            ).build()
                            .createMiauthRepository()
                    event.scope.launch {
                        ktorfitClient.createApp(
                            appCreateRequestBody = AppCreateRequestBody(
                                name = "Mint",
                                description = "Mint",
                                permission = listOf(),
                            )
                        )
                    }
                    authState = AuthStateType.WAITING
                }

                is LoginScreen.Event.OnAuthButtonClicked -> {
                    event.scope.launch {
                        val ktorfitClient =
                            ktorfit.httpClient(httpClient).baseUrl("${domain}/")
                                .converterFactories(
                                    FlowConverterFactory(),
                                    ResponseConverterFactory()
                                ).build()
                                .createMiauthRepository()
                        ktorfitClient.miauth(session)
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
