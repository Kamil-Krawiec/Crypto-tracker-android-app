// app/src/main/java/com/example/cryptotracker/ui/notifications/NotificationsViewModel.kt
package com.example.cryptotracker.ui.notifications

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.cryptotracker.data.entity.PriceAlert
import com.example.cryptotracker.data.repository.AlertRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class NotificationsViewModel(
    private val repo: AlertRepository
) : ViewModel() {

    /** Compose can call collectAsState() on this **/
    val alerts: StateFlow<List<PriceAlert>> =
        repo.getAllAlerts()
            .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    @RequiresApi(Build.VERSION_CODES.O)
    fun markSeen(alert: PriceAlert) {
        viewModelScope.launch {
            repo.markAlertSeen(alert.id)
        }
    }

    fun deleteAlert(alert: PriceAlert) {
        viewModelScope.launch {
            repo.deleteAlert(alert)
        }
    }

    class Factory(private val repo: AlertRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(NotificationsViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return NotificationsViewModel(repo) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}