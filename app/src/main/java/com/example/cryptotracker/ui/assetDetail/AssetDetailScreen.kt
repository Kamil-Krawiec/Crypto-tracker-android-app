package com.example.cryptotracker.ui.assetDetail

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.math.abs

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AssetDetailScreen(
    symbol: String,
    onBack:    () -> Unit,
    onEdit:    (purchaseId: Long) -> Unit,
    onDelete:  (item: PurchaseItem) -> Unit,
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
            contentPadding       = padding,
            verticalArrangement  = Arrangement.spacedBy(8.dp),
            modifier             = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            val dtf = DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm")
            items(items, key = { it.id }) { p ->
                Card(
                    modifier  = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(Modifier.padding(12.dp)) {
                        Text(dtf.format(p.purchaseTimestamp.atZone(ZoneId.systemDefault())))
                        Spacer(Modifier.height(4.dp))
                        Text("Qty: ${p.quantity} @ $${"%.2f".format(p.purchasePrice)}")
                        Spacer(Modifier.height(4.dp))
                        Text("Value: $${"%.2f".format(p.currentValue)}")
                        Spacer(Modifier.height(4.dp))
                        val pl = p.profitLoss
                        val pct = p.percentChange
                        Text(
                            "P/L: ${if (pl >= 0) "+" else "-"}$${"%.2f".format(abs(pl))} (${ "%.1f".format(pct)}%)",
                            color = if (pl >= 0)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.error
                        )

                        Spacer(Modifier.height(8.dp))

                        Row(
                            horizontalArrangement = Arrangement.End,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            TextButton(onClick = { onEdit(p.id) }) {
                                Icon(Icons.Default.Edit, contentDescription = null)
                                Spacer(Modifier.width(4.dp))
                                Text("Edit")
                            }
                            TextButton(onClick = { onDelete(p) }) {
                                Icon(Icons.Default.Delete, contentDescription = null)
                                Spacer(Modifier.width(4.dp))
                                Text("Delete")
                            }
                        }
                    }
                }
            }
        }
    }
}