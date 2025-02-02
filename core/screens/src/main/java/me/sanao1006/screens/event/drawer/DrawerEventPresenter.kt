package me.sanao1006.screens.event.drawer

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuitx.effects.LaunchedImpressionEffect
import javax.inject.Inject
import kotlinx.coroutines.launch
import me.sanao1006.core.data.compositionLocal.LocalNavigator
import me.sanao1006.core.data.util.suspendRunCatching
import me.sanao1006.core.domain.home.LogOutUseCase
import me.sanao1006.core.domain.home.UpdateAccountUseCase
import me.sanao1006.core.model.LoginUserInfo
import me.sanao1006.screens.AnnouncementScreen
import me.sanao1006.screens.AntennaScreen
import me.sanao1006.screens.ChannelListScreen
import me.sanao1006.screens.FavoritesScreen
import me.sanao1006.screens.LoginScreen
import me.sanao1006.screens.SearchScreen
import me.sanao1006.screens.UserScreen
import me.snao1006.res_value.ResString

data class DrawerState(
    val loginUserInfo: LoginUserInfo,
    val expandDialog: Boolean,
    val eventSink: (DrawerEvent) -> Unit
) : CircuitUiState

class DrawerEventPresenter @Inject constructor(
    private val updateAccountUseCase: UpdateAccountUseCase,
    private val logOutUseCase: LogOutUseCase
) : Presenter<DrawerState> {
    @Composable
    override fun present(): DrawerState {
        val navigator = LocalNavigator.current
        val context = LocalContext.current
        val scope = rememberCoroutineScope()
        var loginUserInfo: LoginUserInfo by rememberRetained {
            mutableStateOf(
                LoginUserInfo()
            )
        }
        var expandDialog by rememberRetained { mutableStateOf(false) }
        LaunchedImpressionEffect {
            loginUserInfo = updateAccountUseCase()
        }

        return DrawerState(
            loginUserInfo = loginUserInfo,
            expandDialog = expandDialog
        ) { event ->
            when (event) {
                DrawerEvent.OnDrawerIconClicked -> {
                    navigator.goTo(
                        UserScreen(
                            userId = loginUserInfo.userId,
                            userName = loginUserInfo.userName,
                            host = loginUserInfo.host,
                            isFromDrawer = true
                        )
                    )
                }

                DrawerEvent.OnDrawerSearchClicked -> {
                    navigator.goTo(SearchScreen)
                }

                DrawerEvent.OnDrawerAnnouncementClicked -> {
                    navigator.goTo(AnnouncementScreen)
                }

                DrawerEvent.OnDrawerFavoriteClicked -> {
                    navigator.goTo(FavoritesScreen)
                }

                DrawerEvent.OnDrawerAntennaClicked -> {
                    navigator.goTo(AntennaScreen)
                }

                DrawerEvent.OnLogOutClicked -> {
                    expandDialog = true
                }

                DrawerEvent.OnLogOutConfirmClicked -> {
                    scope.launch {
                        suspendRunCatching {
                            logOutUseCase.invoke()
                        }
                            .onSuccess {
                                navigator.resetRoot(LoginScreen)
                            }
                            .onFailure {
                                Toast.makeText(
                                    context,
                                    context.getString(ResString.log_out_failed),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        expandDialog = false
                    }
                }

                DrawerEvent.OnDismissRequest -> {
                    expandDialog = false
                }

                DrawerEvent.OnDrawerChannelClicked -> {
                    navigator.goTo(ChannelListScreen)
                }

                else -> {}
            }
        }
    }
}
