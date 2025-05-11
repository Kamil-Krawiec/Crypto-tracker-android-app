// app/src/main/java/com/example/cryptotracker/ui/home/HomeScreen.kt
package com.example.cryptotracker.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onAlertsClick:    () -> Unit,
    onPortfolioClick: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState(initial = HomeUiState(isLoading = true))

    Scaffold(
        topBar = {
            TopAppBar(
                title   = { Text("Market Overview") },
                actions = {
                    IconButton(onClick = onAlertsClick) {
                        Icon(Icons.Default.Notifications, contentDescription = "Alerts")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onPortfolioClick) {
                Icon(Icons.Default.ShowChart, contentDescription = "Go to Portfolio")
            }
        }
    ) { padding ->
        Box(
            Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when {
                state.isLoading -> {
                    CircularProgressIndicator(Modifier.align(Alignment.Center))
                }
                state.errorMessage != null -> {
                    Text(
                        text = state.errorMessage!!,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                else -> {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        items(state.items, key = { it.symbol }) { item ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        viewModel.refresh()
                                    }
                            ) {
                                Row(
                                    Modifier
                                        .padding(16.dp)
                                        .fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(item.symbol, style = MaterialTheme.typography.titleMedium)
                                    Text(
                                        text = "$${"%.2f".format(item.livePrice)}",
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}