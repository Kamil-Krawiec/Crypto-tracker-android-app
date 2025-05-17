// app/src/main/java/com/example/cryptotracker/ui/dashboard/AssetDetailScreen.kt
package com.example.cryptotracker.ui.dashboard

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AssetDetailScreen(
    symbol: String,
    onBack: () -> Unit,
    viewModel: AssetDetailViewModel = hiltViewModel()
) {
    val items by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                title = { Text("Purchases: $symbol") }
            )
        }
    ) { padding ->
        LazyColumn(
            contentPadding = padding,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            val dtf = DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm")
            items(items, key = { it.id }) { p ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(Modifier.padding(12.dp)) {
                        Text(
                            dtf.format(p.purchaseTimestamp.atZone(ZoneId.systemDefault())),
                            style = MaterialTheme.typography.bodySmall
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            "Qty: ${p.quantity} @ $${"%.2f".format(p.purchasePrice)}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            "Value: $${"%.2f".format(p.currentValue)}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(Modifier.height(4.dp))
                        val pl  = p.profitLoss
                        val pct = p.percentChange
                        Text(
                            "P/L: ${if (pl >= 0) "+" else "-"}$${"%.2f".format(kotlin.math.abs(pl))} (${ "%.1f".format(pct)}%)",
                            color = if (pl >= 0)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        }
    }
}