package com.example.cryptotracker.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.cryptotracker.data.repository.CryptoRepository
import com.example.cryptotracker.data.repository.Resource
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class HomeItem(
    val symbol: String,
    val livePrice: Double
)

data class HomeUiState(
    val isLoading: Boolean = true,
    val items: List<HomeItem> = emptyList(),
    val errorMessage: String? = null
)

class HomeViewModel(
    private val repo: CryptoRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repo.getLivePrices().collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _uiState.value = HomeUiState(isLoading = true)
                    }
                    is Resource.Success -> {
                        // map each "SYMBOLUSDT" → HomeItem("SYMBOL", price)
                        val items = result.data
                            .mapNotNull { (symbol, price) ->
                                symbol.takeIf { it.endsWith("USDT") }?.removeSuffix("USDT")?.let {
                                    HomeItem(it, price)
                                }
                            }
                        _uiState.value = HomeUiState(isLoading = false, items = items)
                    }
                    is Resource.Error -> {
                        _uiState.value = HomeUiState(
                            isLoading = false,
                            errorMessage = result.exception.localizedMessage
                        )
                    }
                }
            }
        }
    }

    class Factory(private val repo: CryptoRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return HomeViewModel(repo) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}