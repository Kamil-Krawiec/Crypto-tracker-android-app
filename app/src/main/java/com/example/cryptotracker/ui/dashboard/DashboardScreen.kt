// app/src/main/java/com/example/cryptotracker/ui/dashboard/DashboardScreen.kt
package com.example.cryptotracker.ui.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cryptotracker.data.repository.CryptoRepository
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    cryptoRepo: CryptoRepository
) {
    // get or create our ViewModel, passing in the repo
    val vm: DashboardViewModel = viewModel(
        factory = DashboardViewModel.Factory(cryptoRepo)
    )
    // collect the UI state
    val items by vm.uiState.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text("My Portfolio") }) }
    ) { padding ->
        LazyColumn(
            contentPadding = padding,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            items(items, key = { it.symbol }) { item ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(item.symbol, style = MaterialTheme.typography.titleMedium)
                            Spacer(Modifier.height(4.dp))
                            Text(
                                "Qty: ${item.quantity} @ $${"%.2f".format(item.purchasePrice)}",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                        val pl = (item.livePrice - item.purchasePrice) * item.quantity
                        Text(
                            text = "${if (pl >= 0) "+" else "-"}$${"%.2f".format(kotlin.math.abs(pl))}",
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