// app/src/main/java/com/example/cryptotracker/ui/assetDetail/EditPurchaseViewModel.kt
package com.example.cryptotracker.ui.assetDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptotracker.data.dao.AssetDao
import com.example.cryptotracker.data.entity.CryptoAsset
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.Instant
import javax.inject.Inject

@HiltViewModel
class EditPurchaseViewModel @Inject constructor(
    private val dao: AssetDao
) : ViewModel() {

    /**
     * Load a single asset (purchase) by its ID
     * so the edit screen can initialize its form.
     */
    fun loadPurchase(id: Long): Flow<CryptoAsset?> =
        dao.getAssetById(id)
            .flowOn(Dispatchers.IO)

    /** Save updated purchase back to the DB */
    fun updatePurchase(asset: CryptoAsset) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.upsert(asset)
        }
    }
}