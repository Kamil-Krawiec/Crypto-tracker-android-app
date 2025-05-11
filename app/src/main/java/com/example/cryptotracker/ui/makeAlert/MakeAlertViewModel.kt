// app/src/main/java/com/example/cryptotracker/ui/makealert/MakeAlertViewModel.kt
package com.example.cryptotracker.ui.makeAlert

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptotracker.data.entity.PriceAlert
import com.example.cryptotracker.data.repository.AlertRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MakeAlertViewModel @Inject constructor(
    private val repo: AlertRepository
) : ViewModel() {

    @RequiresApi(Build.VERSION_CODES.O)
    fun saveAlert(symbol: String, threshold: Double, isAbove: Boolean) {
        viewModelScope.launch {
            repo.upsertAlert(
                PriceAlert(
                    symbol           = symbol,
                    targetPrice      = threshold,
                    isAboveThreshold = isAbove
                )
            )
        }
    }
}