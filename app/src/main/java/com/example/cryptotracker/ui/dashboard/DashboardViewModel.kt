// app/src/main/java/com/example/cryptotracker/ui/dashboard/DashboardViewModel.kt
package com.example.cryptotracker.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptotracker.data.repository.CryptoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val repo: CryptoRepository
) : ViewModel() {

    /** One‐off lookups of all assets, grouped & averaged by symbol, with live price */
    @OptIn(ExperimentalCoroutinesApi::class)
    val summaryUiState: StateFlow<List<SummaryItem>> =
        repo.getAllAssets()  // Flow<List<CryptoAsset>>
            .flatMapLatest { assets ->
                flow {
                    val grouped = assets.groupBy { it.symbol }
                    val summaries = grouped.map { (symbol, txns) ->
                        val totalQty = txns.sumOf { it.quantity }
                        val avgPrice = if (totalQty > 0)
                            txns.sumOf { it.quantity * it.purchasePrice } / totalQty
                        else 0.0
                        val live = repo.getLivePrice(symbol)
                        SummaryItem(symbol, totalQty, avgPrice, live)
                    }
                    emit(summaries)
                }
            }
            .stateIn(
                viewModelScope,
                SharingStarted.Lazily,
                initialValue = emptyList()
            )
}