// app/src/main/java/com/example/cryptotracker/ui/notifications/NotificationsScreen.kt
package com.example.cryptotracker.ui.notifications

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cryptotracker.data.repository.AlertRepository

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsScreen(
    alertRepo: AlertRepository,
    onAddClick: () -> Unit = {},
    onAlertClick: (Long) -> Unit = {},       // pass alertId
    onDeleteClick: (Long) -> Unit = {}       // pass alertId
) {
    // 1) get our ViewModel
    val viewModel: NotificationsViewModel = viewModel(
        factory = NotificationsViewModel.Factory(alertRepo)
    )
    // 2) collect the StateFlow
    val alerts by viewModel.alerts.collectAsState()

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
                                .weight(1f)
                                .clickable {
                                    viewModel.markSeen(alert)
                                    onAlertClick(alert.id)
                                }
                        ) {
                            Text(alert.symbol, style = MaterialTheme.typography.titleMedium)
                            Spacer(Modifier.height(4.dp))
                            val op = if (alert.isAboveThreshold) "≥" else "≤"
                            Text(
                                "$op $${"%.2f".format(alert.targetPrice)}",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                        IconButton(onClick = {
                            viewModel.deleteAlert(alert)
                            onDeleteClick(alert.id)
                        }) {
                            Icon(Icons.Filled.Delete, contentDescription = "Delete")
                        }
                    }
                }
            }
        }
    }
}