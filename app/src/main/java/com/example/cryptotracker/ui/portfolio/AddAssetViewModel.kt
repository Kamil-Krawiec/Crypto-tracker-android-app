// app/src/main/java/com/example/cryptotracker/ui/portfolio/AddAssetViewModel.kt
package com.example.cryptotracker.ui.portfolio

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptotracker.data.entity.CryptoAsset
import com.example.cryptotracker.data.repository.CryptoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.Instant
import javax.inject.Inject

data class AddAssetUiState(
    val symbol: String    = "",
    val qtyText: String   = "",
    val priceText: String = ""
) {
    val qty: Double?   get() = qtyText.toDoubleOrNull()
    val price: Double? get() = priceText.toDoubleOrNull()
    val isValid: Boolean
        get() = symbol.isNotBlank() && qty != null && price != null
}

@HiltViewModel
class AddAssetViewModel @Inject constructor(
    private val repo: CryptoRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(AddAssetUiState())
    val uiState: StateFlow<AddAssetUiState> = _uiState

    fun onSymbolChange(s: String)   = _uiState.update { it.copy(symbol = s.uppercase()) }
    fun onQtyChange(q: String)      = _uiState.update { it.copy(qtyText = q) }
    fun onPriceChange(p: String)    = _uiState.update { it.copy(priceText = p) }

    @RequiresApi(Build.VERSION_CODES.O)
    fun saveAsset() {
        val st = _uiState.value
        if (st.isValid) {
            viewModelScope.launch {
                repo.upsertAsset(
                    CryptoAsset(
                        symbol             = st.symbol,
                        quantity           = st.qty!!,
                        purchasePrice      = st.price!!,
                        purchaseTimestamp  = Instant.now(),
                        name               = "",
                        imageUrl           = null
                    )
                )
            }
        }
    }
}