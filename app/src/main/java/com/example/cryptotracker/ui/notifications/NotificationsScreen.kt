// app/src/main/java/com/example/cryptotracker/ui/notifications/NotificationsScreen.kt
package com.example.cryptotracker.ui.notifications

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

private enum class AlertFilter { All, Unseen, Triggered }

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsScreen(
    onAddClick:    () -> Unit,
    onAlertClick:  (Long) -> Unit,
    onDeleteClick: (Long) -> Unit
) {
    val vm: NotificationsViewModel = hiltViewModel()
    val alerts by vm.alerts.collectAsState()

    var filter by remember { mutableStateOf(AlertFilter.All) }

    val filtered = when (filter) {
        AlertFilter.All       -> alerts
        AlertFilter.Unseen    -> alerts.filter { it.triggeredAt != null && !it.seen }
        AlertFilter.Triggered -> alerts.filter { it.triggeredAt != null }
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Price Alerts") },
                actions = {
                    IconButton(onClick = { vm.refreshAlerts() }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Refresh")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddClick) {
                Icon(Icons.Filled.Add, contentDescription = "Add Alert")
            }
        }
    ) { padding ->
        Column(Modifier.fillMaxSize().padding(padding)) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = filter == AlertFilter.All,
                    onClick  = { filter = AlertFilter.All },
                    label    = { Text("All") }
                )
                FilterChip(
                    selected = filter == AlertFilter.Unseen,
                    onClick  = { filter = AlertFilter.Unseen },
                    label    = { Text("Unseen") }
                )
                FilterChip(
                    selected = filter == AlertFilter.Triggered,
                    onClick  = { filter = AlertFilter.Triggered },
                    label    = { Text("Triggered") }
                )
            }


            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filtered, key = { it.id }) { alert ->
                    val background = if (alert.triggeredAt != null && !alert.seen)
                        MaterialTheme.colorScheme.errorContainer
                    else
                        MaterialTheme.colorScheme.surface

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(background)
                    ) {
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
                                        if (alert.triggeredAt != null && !alert.seen) {
                                            vm.markSeen(alert)
                                        }
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
                                vm.deleteAlert(alert)
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
}