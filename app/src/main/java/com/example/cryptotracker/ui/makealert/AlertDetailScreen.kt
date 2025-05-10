package com.example.cryptotracker.ui.makealert

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.cryptotracker.data.entity.PriceAlert
import com.example.cryptotracker.data.repository.AlertRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertDetailScreen(
    alertId: Long,
    alertRepo: AlertRepository,
    onBack: () -> Unit
) {
    // load the alert from your repo
    val alert by produceState(
        initialValue = null as PriceAlert?,
        key1 = alertId
    ) {
        value = alertRepo.getAllAlertsOnce().find { it.id == alertId }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Alert Details") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        alert?.let { a ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
            ) {
                Text("Symbol: ${a.symbol}", style = MaterialTheme.typography.titleLarge)
                Spacer(Modifier.height(8.dp))
                Text("Target Price: ${a.targetPrice}", style = MaterialTheme.typography.bodyLarge)
                Spacer(Modifier.height(8.dp))
                Text(
                    "When ${if (a.isAboveThreshold) "above" else "below"} threshold",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(Modifier.height(8.dp))
                Text("Created: ${a.createdAt}", style = MaterialTheme.typography.bodySmall)
                a.triggeredAt?.let {
                    Spacer(Modifier.height(8.dp))
                    Text("Triggered: $it", style = MaterialTheme.typography.bodySmall)
                }
            }
        } ?: run {
            // still loading or not found
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
    }
}