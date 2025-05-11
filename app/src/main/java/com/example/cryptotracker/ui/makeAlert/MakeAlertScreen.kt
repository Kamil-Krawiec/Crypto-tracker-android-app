// app/src/main/java/com/example/cryptotracker/ui/makealert/MakeAlertScreen.kt
package com.example.cryptotracker.ui.makeAlert

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
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MakeAlertScreen(
    onDone: () -> Unit
) {
    val vm: MakeAlertViewModel = hiltViewModel()
    val snackbarHostState    = remember { SnackbarHostState() }
    val coroutineScope       = rememberCoroutineScope()

    var symbol        by remember { mutableStateOf("") }
    var thresholdText by remember { mutableStateOf("") }
    var isAbove       by remember { mutableStateOf(true) }

    Scaffold(
        topBar      = { TopAppBar(title = { Text("New Price Alert") }) },
        snackbarHost= { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value       = symbol,
                onValueChange = { symbol = it },
                label       = { Text("Symbol (e.g. BTC)") },
                singleLine  = true,
                modifier    = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value       = thresholdText,
                onValueChange = { thresholdText = it },
                label       = { Text("Target Price") },
                singleLine  = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction    = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { /* hide keyboard if desired */ }
                ),
                modifier    = Modifier.fillMaxWidth()
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Notify when price ")
                Switch(
                    checked         = isAbove,
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
                            snackbarHostState.showSnackbar("Please enter valid symbol & price")
                        }
                    } else {
                        vm.saveAlert(symbol.uppercase(), threshold, isAbove)
                        onDone()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save Alert")
            }
        }
    }
}