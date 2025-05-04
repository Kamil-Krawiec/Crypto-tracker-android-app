// app/src/main/java/com/example/cryptotracker/ui/home/HomeViewModel.kt
package com.example.cryptotracker.ui.home

import androidx.lifecycle.*
import com.example.cryptotracker.data.entity.CryptoAsset
import com.example.cryptotracker.data.repository.CryptoRepository
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repo: CryptoRepository
) : ViewModel() {

    val assets: LiveData<List<CryptoAsset>> =
        repo.getAllAssets().asLiveData()

    fun addAsset(symbol: String, qty: Double, price: Double) {
        viewModelScope.launch {
            repo.upsert(
                CryptoAsset(symbol = symbol, quantity = qty, purchasePrice = price)
            )
        }
    }

    fun deleteAsset(asset: CryptoAsset) {
        viewModelScope.launch {
            repo.delete(asset)
        }
    }

    /** Factory to inject repo */
    class Factory(private val repo: CryptoRepository) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            HomeViewModel(repo) as T
    }
}