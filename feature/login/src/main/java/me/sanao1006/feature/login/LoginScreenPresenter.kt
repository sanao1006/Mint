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
import me.sanao1006.core.data.repository.createMiauthRepository
import me.sanao1006.core.model.auth.AppCreateRequestBody
import me.sanao1006.core.model.auth.AuthSessionGenerateRequestBody
import me.sanao1006.core.model.auth.AuthSessionUserKeyRequestBody
import me.sanao1006.core.model.auth.PermissionKeys
import me.sanao1006.datastore.DataStoreRepository
import java.security.MessageDigest
import javax.inject.Inject

@CircuitInject(LoginScreen::class, SingletonComponent::class)
class LoginScreenPresenter @Inject constructor(
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
        var authState by rememberRetained { mutableStateOf(AuthStateType.FIXED) }

        return LoginScreen.State(
            domain = domain,
            authState = authState
        ) { event ->
            when (event) {
                is LoginScreen.Event.OnTextChanged -> {
                    domain = event.text
                }

                is LoginScreen.Event.OnButtonClicked -> {
                    val ktorfitClient = ktorfit
                        .baseUrl("${domain}/")
                        .build()
                        .createMiauthRepository()
                    event.scope.launch {
                        val appCreate = ktorfitClient.createApp(
                            appCreateRequestBody = AppCreateRequestBody(
                                name = "Mint",
                                description = "Mint",
                                permission = PermissionKeys.getAllPermissions(),
                                callbackUrl = "myapp://auth-callback"
                            )
                        )
                        secret = appCreate.secret
                        val sessionResponse = ktorfitClient.authSessionGenerate(
                            authSessionGenerateRequestBody = AuthSessionGenerateRequestBody(
                                appSecret = appCreate.secret
                            )
                        )
                        token = sessionResponse.token
                        openUrlInChrome(url = sessionResponse.url, context = event.context)
                    }
                    authState = AuthStateType.WAITING
                }

                is LoginScreen.Event.OnAuthButtonClicked -> {
                    event.scope.launch {
                        val ktorfitClient = ktorfit
                            .baseUrl("${domain}/")
                            .build()
                            .createMiauthRepository()

                        val authSessionResponse = ktorfitClient.authSessionUserKey(
                            authSessionUserKeyRequestBody = AuthSessionUserKeyRequestBody(
                                appSecret = secret,
                                token = token
                            )
                        )
                        val accessToken = (authSessionResponse.accessToken + secret).toSHA256()
                        dataStoreRepository.saveAccessToken(accessToken)
                        dataStoreRepository.saveBaseUrl(domain)
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

enum class AuthStateType { FIXED, WAITING, SUCCESS }
