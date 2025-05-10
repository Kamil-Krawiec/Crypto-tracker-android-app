package com.example.cryptotracker.ui.notifications

import androidx.lifecycle.*
import com.example.cryptotracker.data.entity.PriceAlert
import com.example.cryptotracker.data.repository.AlertRepository
import kotlinx.coroutines.launch

class NotificationsViewModel(
    private val repo: AlertRepository
) : ViewModel() {

    val alerts: LiveData<List<PriceAlert>> =
        repo.getAllAlerts().asLiveData()

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

    class Factory(private val repo: AlertRepository) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            NotificationsViewModel(repo) as T
    }
}