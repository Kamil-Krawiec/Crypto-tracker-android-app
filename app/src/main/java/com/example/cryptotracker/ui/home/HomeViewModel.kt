package com.example.cryptotracker.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptotracker.data.repository.CryptoRepository
import com.example.cryptotracker.data.repository.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repo.getLivePrices().collect { result ->
                when (result) {
                    is Resource.Loading -> _uiState.value = HomeUiState(isLoading = true)
                    is Resource.Success -> {
                        val items = result.data
                            .mapNotNull { (symbol, price) ->
                                symbol.takeIf { it.endsWith("USDT") }
                                    ?.removeSuffix("USDT")
                                    ?.let { HomeItem(it, price) }
                            }
                        _uiState.value = HomeUiState(isLoading = false, items = items)
                    }
                    is Resource.Error -> _uiState.value = HomeUiState(
                        isLoading = false,
                        errorMessage = result.exception.localizedMessage
                    )
                }
            }
        }
    }

}