// app/src/main/java/com/example/cryptotracker/ui/dashboard/AssetDetailViewModel.kt
package com.example.cryptotracker.ui.dashboard

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptotracker.data.repository.CryptoRepository
import com.example.cryptotracker.ui.assetDetail.PurchaseItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AssetDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repo: CryptoRepository
) : ViewModel() {
    // pulled from NavGraph arg
    private val symbol: String = checkNotNull(savedStateHandle["symbol"])

    // one‐off fetch of current price
    private val livePriceFlow: Flow<Double> = flow {
        emit(repo.getLivePrice(symbol))
    }.flowOn(Dispatchers.IO)

    /**
     * Emits a list of PurchaseItem whenever the DB (purchases) or live price updates
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<List<PurchaseItem>> =
        repo.getPurchases(symbol)
            .combine(livePriceFlow) { purchases, live ->
                purchases.map { asset ->
                    PurchaseItem(
                        id                = asset.id,
                        symbol            = asset.symbol,
                        name              = asset.name,
                        quantity          = asset.quantity,
                        purchasePrice     = asset.purchasePrice,
                        purchaseTimestamp = asset.purchaseTimestamp,
                        livePrice         = live,
                        imageUrl          = asset.imageUrl
                    )
                }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Lazily,
                initialValue = emptyList()
            )
}