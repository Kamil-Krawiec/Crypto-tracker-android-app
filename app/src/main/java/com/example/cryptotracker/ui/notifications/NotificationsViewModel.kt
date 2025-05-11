// app/src/main/java/com/example/cryptotracker/ui/notifications/NotificationsViewModel.kt
package com.example.cryptotracker.ui.notifications

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptotracker.data.repository.AlertRepository
import com.example.cryptotracker.data.entity.PriceAlert
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationsViewModel @Inject constructor(
    private val repo: AlertRepository
) : ViewModel() {

    /** Compose can collect this via collectAsState() **/
    val alerts: StateFlow<List<PriceAlert>> =
        repo.getAllAlerts()
            .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    @RequiresApi(Build.VERSION_CODES.O)
    fun markSeen(alert: PriceAlert) = viewModelScope.launch {
        repo.markAlertSeen(alert.id)
    }

    fun deleteAlert(alert: PriceAlert) = viewModelScope.launch {
        repo.deleteAlert(alert)
    }
    /** Kick off a one-off check right now */
    @RequiresApi(Build.VERSION_CODES.O)
    fun refreshAlerts() = viewModelScope.launch {
        repo.checkAlerts()
    }
}