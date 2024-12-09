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
import de.jensklingenberg.ktorfit.Ktorfit
import de.jensklingenberg.ktorfit.converter.FlowConverterFactory
import de.jensklingenberg.ktorfit.converter.ResponseConverterFactory
import io.ktor.client.HttpClient
import kotlinx.coroutines.launch
import me.sanao1006.core.data.repository.createMiauthRepository
import me.sanao1006.core.data.util.suspendRunCatching
import me.sanao1006.core.model.LoginUserInfo
import me.sanao1006.core.model.NormalApi
import me.sanao1006.core.model.auth.AppCreateRequestBody
import me.sanao1006.core.model.auth.AuthSessionGenerateRequestBody
import me.sanao1006.core.model.auth.AuthSessionUserKeyRequestBody
import me.sanao1006.core.model.auth.PermissionKeys
import me.sanao1006.datastore.DataStoreRepository
import me.sanao1006.screens.AuthStateType
import me.sanao1006.screens.LoginScreen
import me.snao1006.res_value.ResString
import java.security.MessageDigest
import javax.inject.Inject

@CircuitInject(LoginScreen::class, SingletonComponent::class)
class LoginScreenPresenter @Inject constructor(
    @NormalApi
    private val httpClient: HttpClient,
    private val dataStoreRepository: DataStoreRepository
) : Presenter<LoginScreen.State> {
    private val ktorfit = Ktorfit.Builder()
        .httpClient(httpClient)
        .converterFactories(
            FlowConverterFactory(),
            ResponseConverterFactory()
        )

    @Composable
    override fun present(): LoginScreen.State {
        var domain by rememberRetained { mutableStateOf("") }
        var secret by rememberRetained { mutableStateOf("") }
        var token by rememberRetained { mutableStateOf("") }
        var buttonEnabled by rememberRetained { mutableStateOf(false) }
        var authState by rememberRetained { mutableStateOf(AuthStateType.FIXED) }
        val scope = rememberCoroutineScope()

        return LoginScreen.State(
            domain = domain,
            buttonEnabled = buttonEnabled,
            authState = authState
        ) { event ->
            when (event) {
                is LoginScreen.Event.OnTextChanged -> {
                    buttonEnabled = event.text.isNotBlank() &&
                            event.text
                                .matches(Regex("^https?://[a-zA-Z0-9]+(\\.[a-zA-Z0-9]+)+\$"))
                    domain = event.text
                }

                is LoginScreen.Event.OnButtonClicked -> {
                    event.scope.launch {
                        suspendRunCatching {
                            ktorfit
                                .baseUrl("$domain/")
                                .build()
                                .createMiauthRepository()
                        }.onSuccess { ktorfitClient ->
                            suspendRunCatching {
                                ktorfitClient.createApp(
                                    appCreateRequestBody = AppCreateRequestBody(
                                        name = "Mint",
                                        description = "Mint",
                                        permission = PermissionKeys.getAllPermissions(),
                                        callbackUrl = "myapp://auth-callback"
                                    )
                                )
                            }
                                .onSuccess { appCreate ->
                                    secret = appCreate.secret
                                    val sessionResponse = ktorfitClient.authSessionGenerate(
                                        authSessionGenerateRequestBody = AuthSessionGenerateRequestBody(
                                            appSecret = appCreate.secret
                                        )
                                    )
                                    token = sessionResponse.token
                                    openUrlInChrome(
                                        url = sessionResponse.url,
                                        context = event.context
                                    )
                                    authState = AuthStateType.WAITING
                                }.onFailure {
                                    Toast.makeText(
                                        event.context,
                                        event.context.getString(
                                            ResString.login_invalid_host,
                                            domain
                                        ),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                        }.onFailure {
                            Toast.makeText(
                                event.context,
                                it.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }

                is LoginScreen.Event.OnAuthButtonClicked -> {
                    scope.launch {
                        suspendRunCatching {
                            ktorfit
                                .baseUrl("$domain/")
                                .build()
                                .createMiauthRepository()
                        }
                            .onSuccess { ktorfitClient ->
                                suspendRunCatching {
                                    ktorfitClient.authSessionUserKey(
                                        authSessionUserKeyRequestBody = AuthSessionUserKeyRequestBody(
                                            appSecret = secret,
                                            token = token
                                        )
                                    )
                                }.onSuccess { authSessionResponse ->
                                    val accessToken =
                                        (authSessionResponse.accessToken + secret).toSHA256()
                                    dataStoreRepository.saveAccessToken(accessToken)
                                    dataStoreRepository.saveBaseUrl(domain)
                                    dataStoreRepository.saveLoginUserInfo(
                                        LoginUserInfo(
                                            userName = authSessionResponse.user.username,
                                            name = authSessionResponse.user.name ?: "",
                                            avatarUrl = authSessionResponse.user.avatarUrl ?: ""
                                        )
                                    )
                                    authState = AuthStateType.SUCCESS
                                }.onFailure {
                                    Toast.makeText(
                                        event.context,
                                        event.context.getString(ResString.login_failed),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    authState = AuthStateType.FIXED
                                }
                            }.onFailure {
                                Toast.makeText(
                                    event.context,
                                    event.context.getString(ResString.login_failed),
                                    Toast.LENGTH_SHORT
                                ).show()
                                authState = AuthStateType.FIXED
                            }
                    }
                }
            }
        }
    }

    companion object {
        private fun String.toSHA256(): String {
            val bytes = MessageDigest.getInstance("SHA-256").digest(this.toByteArray())
            return bytes.fold("", { str, it -> str + "%02x".format(it) })
        }

        private fun openUrlInChrome(url: String, context: Context) {
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
    }
}
