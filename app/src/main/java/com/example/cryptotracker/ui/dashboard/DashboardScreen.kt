// app/src/main/java/com/example/cryptotracker/ui/dashboard/DashboardScreen.kt
package com.example.cryptotracker.ui.dashboard

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlin.math.abs

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    onBack: () -> Unit,
    onAdd: () -> Unit,
    onSymbolClick: (String) -> Unit,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val items by viewModel.summaryUiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Portfolio") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null)
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAdd) {
                Icon(Icons.Default.Add, contentDescription = "Add Asset")
            }
        }
    ) { padding ->
        LazyColumn(
            contentPadding = padding,
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            items(items, key = { it.symbol }) { summary ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onSymbolClick(summary.symbol) }
                        .animateContentSize(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // simple circle icon
                        Surface(
                            shape = CircleShape,
                            tonalElevation = 4.dp,
                            color = MaterialTheme.colorScheme.primaryContainer,
                            modifier = Modifier.size(48.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.AttachMoney,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                        }

                        Spacer(Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            // Symbol + total value
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(summary.symbol, style = MaterialTheme.typography.titleMedium)
                                Text(
                                    text = "$${"%.2f".format(summary.currentValue)}",
                                    style = MaterialTheme.typography.titleSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            Spacer(Modifier.height(4.dp))

                            // Avg purchase price & qty
                            Text(
                                text = "Avg: $${"%.2f".format(summary.averagePrice)}",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                text = "Qty: ${summary.totalQuantity}",
                                style = MaterialTheme.typography.bodyMedium
                            )

                            Spacer(Modifier.height(8.dp))

                            // P/L + % return
                            val plColor = if (summary.profitLoss >= 0)
                                MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.error
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "${if (summary.profitLoss >= 0) "+" else "-"}$${"%.2f".format(abs(summary.profitLoss))}",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = plColor
                                )
                                Spacer(Modifier.width(8.dp))
                                Text(
                                    text = "${"%.1f".format(summary.percentChange)}%",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = plColor
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}