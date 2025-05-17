// app/src/main/java/com/example/cryptotracker/ui/portfolio/AddAssetViewModel.kt
package com.example.cryptotracker.ui.portfolio

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptotracker.data.entity.CryptoAsset
import com.example.cryptotracker.data.repository.CryptoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.Instant
import javax.inject.Inject

@HiltViewModel
class AddAssetViewModel @Inject constructor(
    private val repo: CryptoRepository
) : ViewModel() {

    /**
     * @param explicitPrice     user‐entered price, or null to fetch historical
     * @param purchaseTimestamp when the asset was bought; if null, defaults to now
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun saveAsset(
        symbol: String,
        name: String,
        quantity: Double,
        explicitPrice: Double?,
        purchaseTimestamp: Instant? = null,
        imageUrl: String?,
    ) {
        viewModelScope.launch {
            // choose timestamp: passed in or now
            val ts = purchaseTimestamp ?: Instant.now()

            // choose price: explicit or historical at that ts
            val price = explicitPrice
                ?: repo.getHistoricalPrice(symbol.uppercase(), ts)

            val asset = CryptoAsset(
                symbol            = symbol.uppercase(),
                name              = name,
                quantity          = quantity,
                purchasePrice     = price,
                purchaseTimestamp = ts,
                imageUrl          = imageUrl
            )
            repo.upsertAsset(asset)
        }
    }
}