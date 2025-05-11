// app/src/main/java/com/example/cryptotracker/ui/dashboard/DashboardViewModel.kt
package com.example.cryptotracker.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptotracker.data.repository.CryptoRepository
import com.example.cryptotracker.data.repository.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import javax.inject.Inject

data class DashboardItem(
    val symbol: String,
    val quantity: Double,
    val purchasePrice: Double,
    val livePrice: Double
)

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val repo: CryptoRepository
) : ViewModel() {

    // emit a list of DashboardItem whenever prices or assets change
    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<List<DashboardItem>> =
        repo.getAllAssets()
            .flatMapLatest { assets ->
                repo.getLivePrices().map { resource ->
                    if (resource is Resource.Success) {
                        val prices = resource.data
                        assets.map { asset ->
                            DashboardItem(
                                symbol        = asset.symbol,
                                quantity      = asset.quantity,
                                purchasePrice = asset.purchasePrice,
                                livePrice     = prices["${asset.symbol.uppercase()}USDT"] ?: 0.0
                            )
                        }
                    } else emptyList()
                }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Lazily,
                initialValue = emptyList()
            )
}