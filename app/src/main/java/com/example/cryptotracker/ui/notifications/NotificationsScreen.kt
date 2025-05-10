package com.example.cryptotracker.ui.notifications

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cryptotracker.data.entity.PriceAlert
import com.example.cryptotracker.data.repository.AlertRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsScreen(
    alertRepo: AlertRepository,
    onAddClick: () -> Unit = {},
    onAlertClick: (PriceAlert) -> Unit = {},
    onDeleteClick: (PriceAlert) -> Unit = {}
) {
    val viewModel: NotificationsViewModel = viewModel(
        factory = NotificationsViewModel.Factory(alertRepo)
    )
    // Observe the LiveData<List<PriceAlert>>
    val alerts by viewModel.alerts.observeAsState(initial = emptyList<PriceAlert>())

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Price Alerts") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddClick) {
                Icon(Icons.Filled.Add, contentDescription = "Add Alert")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(alerts, key = { it.id }) { alert ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(
                            modifier = Modifier
                                .clickable { onAlertClick(alert) }
                        ) {
                            Text(alert.symbol, style = MaterialTheme.typography.titleMedium)
                            Spacer(Modifier.height(4.dp))
                            val op = if (alert.isAboveThreshold) "≥" else "≤"
                            Text(
                                "$op $${"%.2f".format(alert.targetPrice)}",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                        IconButton(onClick = { onDeleteClick(alert) }) {
                            Icon(Icons.Filled.Delete, contentDescription = "Delete")
                        }
                    }
                }
            }
        }
    }
}