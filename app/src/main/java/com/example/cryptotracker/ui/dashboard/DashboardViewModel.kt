// app/src/main/java/com/example/cryptotracker/ui/dashboard/DashboardViewModel.kt
package com.example.cryptotracker.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.cryptotracker.data.repository.CryptoRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class DashboardItem(
    val symbol: String,
    val quantity: Double,
    val purchasePrice: Double,
    val livePrice: Double
)

class DashboardViewModel(
    private val repo: CryptoRepository
) : ViewModel() {

    /** Emits a new List<DashboardItem> whenever assets or their live prices change */
    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<List<DashboardItem>> =
        repo.getAllAssets()
            .flatMapLatest { assets ->
                flow {
                    val items = assets.map { asset ->
                        DashboardItem(
                            symbol        = asset.symbol,
                            quantity      = asset.quantity,
                            purchasePrice = asset.purchasePrice,
                            livePrice     = repo.getLivePrice(asset.symbol)
                        )
                    }
                    emit(items)
                }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Lazily,
                initialValue = emptyList()
            )

    /** Factory so Compose can create this VM with our repo */
    class Factory(private val repo: CryptoRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(DashboardViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return DashboardViewModel(repo) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}