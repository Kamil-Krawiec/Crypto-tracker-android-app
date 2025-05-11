package com.example.cryptotracker.ui.alertDetail

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlin.text.format
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertDetailScreen(
    onBack: () -> Unit,
    viewModel: AlertDetailViewModel = hiltViewModel()
) {
    val alert by viewModel.alert.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Alert Details") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    alert?.let {
                        IconButton(onClick = { viewModel.deleteAlert(); onBack() }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete")
                        }
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
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("Symbol: ${a.symbol}", style = MaterialTheme.typography.titleLarge)
                Text("Target: $${"%.2f".format(a.targetPrice)}",
                    style = MaterialTheme.typography.bodyLarge)
                Text("When ${if (a.isAboveThreshold) "above" else "below"} threshold",
                    style = MaterialTheme.typography.bodyMedium)
                Text("Created: ${a.createdAt}", style = MaterialTheme.typography.bodySmall)
                a.triggeredAt?.let {
                    Text("Triggered: $it", style = MaterialTheme.typography.bodySmall)
                }

                Spacer(Modifier.weight(1f))

                // only show checkbox if it actually fired
                if (a.triggeredAt != null) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = a.seen,
                            onCheckedChange = { checked ->
                                if (checked && !a.seen) {
                                    viewModel.markSeen()
                                }
                            }
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            if (a.seen) "Seen" else "Mark as seen",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        } ?: run {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
    }
}