// app/src/main/java/com/example/cryptotracker/ui/alert/AlertDetailViewModel.kt
package com.example.cryptotracker.ui.alertDetail

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptotracker.data.entity.PriceAlert
import com.example.cryptotracker.data.repository.AlertRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlertDetailViewModel @Inject constructor(
    private val repo: AlertRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val alertId: Long = checkNotNull(savedStateHandle["alertId"])

    /** UI state holding loading + the alert or error **/
    private val _uiState = MutableStateFlow<PriceAlert?>(null)
    val alert: StateFlow<PriceAlert?> = _uiState.asStateFlow()

    init {
        repo.getAlertById(alertId)
            .onEach { _uiState.value = it }
            .launchIn(viewModelScope)
    }

    fun deleteAlert() = viewModelScope.launch {
        _uiState.value?.let { repo.deleteAlert(it) }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun markSeen() = viewModelScope.launch {
        repo.markAlertSeen(alertId)
    }
}