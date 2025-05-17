// app/src/main/java/com/example/cryptotracker/ui/dashboard/AssetDetailViewModel.kt
package com.example.cryptotracker.ui.dashboard

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptotracker.data.entity.CryptoAsset
import com.example.cryptotracker.data.repository.CryptoRepository
import com.example.cryptotracker.ui.assetDetail.PurchaseItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import java.time.Instant

@HiltViewModel
class AssetDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repo: CryptoRepository
) : ViewModel() {
    // from NavGraph arg
    private val symbol: String = checkNotNull(savedStateHandle["symbol"])

    // one-off live price fetch
    private val livePriceFlow = flow {
        emit(repo.getLivePrice(symbol))
    }.flowOn(Dispatchers.IO)

    /** Emits a list of PurchaseItem whenever the DB or live price updates */
    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<List<PurchaseItem>> =
        repo.getPurchases(symbol)               // Flow<List<CryptoAsset>>
            .combine(livePriceFlow) { list, live ->
                list.map { asset ->
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
            .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    /** Delete a single purchase */
    fun deletePurchase(item: PurchaseItem) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.deleteAsset(
                CryptoAsset(
                    id                = item.id,
                    symbol            = item.symbol,
                    name              = item.name,
                    quantity          = item.quantity,
                    purchasePrice     = item.purchasePrice,
                    purchaseTimestamp = item.purchaseTimestamp,
                    imageUrl          = item.imageUrl
                )
            )
        }
    }

    /** Update an existing purchase (e.g. after editing) */
    fun updatePurchase(item: PurchaseItem) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.upsertAsset(
                CryptoAsset(
                    id                = item.id,
                    symbol            = item.symbol,
                    name              = item.name,
                    quantity          = item.quantity,
                    purchasePrice     = item.purchasePrice,
                    purchaseTimestamp = item.purchaseTimestamp,
                    imageUrl          = item.imageUrl
                )
            )
        }
    }
}