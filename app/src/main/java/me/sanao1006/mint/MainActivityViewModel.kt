package me.sanao1006.mint

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import me.sanao1006.datastore.DataStoreRepository

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val dataStoreRepository: DataStoreRepository
) : ViewModel() {
    val uiState: StateFlow<UiState> = dataStoreRepository.flowAccessToken().map {
        UiState(
            tokenLoadingState = true,
            isLoggedIn = it.isNotBlank()
        )
    }.stateIn(
        scope = viewModelScope,
        initialValue = UiState(
            tokenLoadingState = false,
            isLoggedIn = false
        ),
        started = SharingStarted.WhileSubscribed(5_000)
    )
}

data class UiState(
    val tokenLoadingState: Boolean,
    val isLoggedIn: Boolean
)
