// app/src/main/java/com/example/cryptotracker/ui/dashboard/DashboardViewModel.kt
package com.example.cryptotracker.ui.dashboard

import androidx.lifecycle.*
import com.example.cryptotracker.data.repository.CryptoRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.map
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope


class DashboardViewModel(
    private val repo: CryptoRepository
) : ViewModel() {

    // convert StateFlow<List<DashboardItem>> into LiveData so Fragment can observe()
    val portfolioUiState: LiveData<List<DashboardItem>> = repo
        .getAllAssets()
        .map { assets ->
            assets.map { asset ->
                val live = repo.getLivePrice(asset.symbol)
                DashboardItem(
                    symbol = asset.symbol,
                    quantity = asset.quantity,
                    purchasePrice = asset.purchasePrice,
                    livePrice = live
                )
            }
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
        .asLiveData()

    /** ViewModel factory to inject repository */
    class Factory(
        private val repo: CryptoRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(DashboardViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return DashboardViewModel(repo) as T
            }
            throw IllegalArgumentException("Unknown VM class")
        }
    }
}