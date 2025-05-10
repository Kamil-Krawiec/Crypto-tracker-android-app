// app/src/main/java/com/example/cryptotracker/ui/makealert/MakeAlertScreen.kt
package com.example.cryptotracker.ui.makealert

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import com.example.cryptotracker.data.entity.PriceAlert
import com.example.cryptotracker.data.repository.AlertRepository

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MakeAlertScreen(
    alertRepo: AlertRepository,
    onDone: () -> Unit
) {
    // create a host for snackbars
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope     = rememberCoroutineScope()

    var symbol        by remember { mutableStateOf("") }
    var thresholdText by remember { mutableStateOf("") }
    var isAbove       by remember { mutableStateOf(true) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("New Price Alert") })
        },
        // hook up our host so we can launch snackbars
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = symbol,
                onValueChange = { symbol = it },
                label = { Text("Symbol (e.g. BTC)") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = thresholdText,
                onValueChange = { thresholdText = it },
                label = { Text("Target Price") },
                singleLine = true,
                // numeric keyboard & IME action
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { /* you can hide keyboard here if you want */ }
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Notify when price ")
                Switch(
                    checked = isAbove,
                    onCheckedChange = { isAbove = it }
                )
                Spacer(Modifier.width(8.dp))
                Text(if (isAbove) "≥ target" else "≤ target")
            }

            Spacer(Modifier.weight(1f))

            Button(
                onClick = {
                    val threshold = thresholdText.toDoubleOrNull()
                    if (symbol.isBlank() || threshold == null) {
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar(
                                "Please enter valid symbol & price"
                            )
                        }
                    } else {
                        coroutineScope.launch {
                            alertRepo.upsertAlert(
                                PriceAlert(
                                    symbol           = symbol.uppercase(),
                                    targetPrice      = threshold,
                                    isAboveThreshold = isAbove
                                )
                            )
                            onDone()
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save Alert")
            }
        }
    }
}