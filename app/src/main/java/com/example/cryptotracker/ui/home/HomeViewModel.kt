// app/src/main/java/com/example/cryptotracker/ui/home/HomeViewModel.kt
package com.example.cryptotracker.ui.home

import androidx.lifecycle.*
import com.example.cryptotracker.data.entity.CryptoAsset
import com.example.cryptotracker.data.repository.CryptoRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repo: CryptoRepository
) : ViewModel() {

    /**
     * Stream of HomeItem(symbol, livePrice)
     * built from the stored assets + live prices from network.
     */
    val assetsUiState: LiveData<List<HomeItem>> = repo.getAllAssets()
        .map { assets ->
            assets.map { asset ->
                // fetch the live price for each symbol
                HomeItem(
                    symbol = asset.symbol,
                    livePrice = repo.getLivePrice(asset.symbol)
                )
            }
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
        .asLiveData()

    /** Add a new tracked asset (not used here, but kept for symmetry) */
    fun addAsset(symbol: String, qty: Double, price: Double) {
        viewModelScope.launch {
            repo.upsertAsset(CryptoAsset(1,symbol, qty, price))
        }
    }

    fun deleteAsset(asset: CryptoAsset) {
        // no-op or implement if you allow deleting from this list
    }

    class Factory(private val repo: CryptoRepository) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return HomeViewModel(repo) as T
            }
            throw IllegalArgumentException("Unknown VM class")
        }
    }
}