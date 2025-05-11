package com.example.cryptotracker.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptotracker.data.repository.CryptoRepository
import com.example.cryptotracker.data.repository.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

data class HomeItem(
    val symbol: String,
    val livePrice: Double
)

data class HomeUiState(
    val isLoading: Boolean = true,
    val items: List<HomeItem> = emptyList(),
    val errorMessage: String? = null
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repo: CryptoRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState(isLoading = true))
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        refresh()
    }

    /** One–off fetch; updates _uiState on each emission */
    fun refresh() {
        viewModelScope.launch {
            repo.getLivePrices()
                .onStart {
                    // Show spinner right away
                    _uiState.value = HomeUiState(isLoading = true)
                }
                .collect { result ->
                    _uiState.value = when (result) {
                        is Resource.Loading -> HomeUiState(isLoading = true)
                        is Resource.Success -> {
                            val items = result.data
                                .mapNotNull { (symbol, price) ->
                                    symbol.takeIf { it.endsWith("USDT") }
                                        ?.removeSuffix("USDT")
                                        ?.let { HomeItem(it, price) }
                                }
                            HomeUiState(isLoading = false, items = items)
                        }
                        is Resource.Error -> HomeUiState(
                            isLoading    = false,
                            errorMessage = result.exception.localizedMessage
                        )
                    }
                }
        }
    }
}