// app/src/main/java/com/example/cryptotracker/ui/home/HomeScreen.kt
package com.example.cryptotracker.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cryptotracker.data.repository.CryptoRepository
import com.example.cryptotracker.ui.home.HomeItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    cryptoRepo: CryptoRepository,
    onPortfolioClick: () -> Unit = {}
) {
    val viewModel: HomeViewModel = viewModel(
        factory = HomeViewModel.Factory(cryptoRepo)
    )

    // **here** we observe the List<HomeItem> from assetsUiState
    val assets by viewModel.assetsUiState.observeAsState(emptyList())

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Market Overview") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onPortfolioClick) {
                Icon(Icons.Default.ShowChart, contentDescription = "Go to Portfolio")
            }
        }
    ) { padding ->
        LazyColumn(
            contentPadding = padding,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            items(assets, key = { it.symbol }) { item ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { /* future detail screen */ }
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