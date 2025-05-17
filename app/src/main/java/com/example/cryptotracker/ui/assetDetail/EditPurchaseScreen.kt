package com.example.cryptotracker.ui.assetDetail

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import java.time.*
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditPurchaseScreen(
    purchaseId: Long,
    onBack:     () -> Unit,
    onSaved:    () -> Unit,
    viewModel:  EditPurchaseViewModel = hiltViewModel()
) {
    val ctx = LocalContext.current

    // load the asset once
    val asset by viewModel
        .loadPurchase(purchaseId)
        .collectAsState(initial = null)

    asset?.let { orig ->
        // date/time formatters
        val dateFmt = DateTimeFormatter.ofPattern("MMM dd, yyyy")
        val timeFmt = DateTimeFormatter.ofPattern("HH:mm")

        // initialize your form state from `orig`
        var qtyText   by remember { mutableStateOf(orig.quantity.toString()) }
        var priceText by remember { mutableStateOf(orig.purchasePrice.toString()) }
        val zdt       = orig.purchaseTimestamp.atZone(ZoneId.systemDefault())
        var date      by remember { mutableStateOf(zdt.toLocalDate()) }
        var time      by remember { mutableStateOf(zdt.toLocalTime().withSecond(0).withNano(0)) }

        val isValid = remember(qtyText, priceText) {
            qtyText.toDoubleOrNull()?.let { it > 0 } == true &&
                    priceText.toDoubleOrNull()?.let { it > 0 } == true
        }

        Scaffold(
            topBar = {
                TopAppBar(
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Cancel")
                        }
                    },
                    title = { Text("Edit Purchase") }
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        if (!isValid) return@FloatingActionButton
                        val updated = orig.copy(
                            quantity          = qtyText.toDouble(),
                            purchasePrice     = priceText.toDouble(),
                            purchaseTimestamp = date
                                .atTime(time)
                                .atZone(ZoneId.systemDefault())
                                .toInstant()
                        )
                        viewModel.updatePurchase(updated)
                        onSaved()
                    },
                ) {
                    Icon(Icons.Default.Save, contentDescription = "Save")
                }
            }
        ) { padding ->
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("Symbol: ${orig.symbol}", style = MaterialTheme.typography.titleMedium)

                OutlinedTextField(
                    value           = qtyText,
                    onValueChange   = { qtyText = it },
                    label           = { Text("Quantity") },
                    singleLine      = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    isError         = qtyText.toDoubleOrNull() == null,
                    modifier        = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value           = priceText,
                    onValueChange   = { priceText = it },
                    label           = { Text("Price (USD)") },
                    singleLine      = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    isError         = priceText.toDoubleOrNull() == null,
                    modifier        = Modifier.fillMaxWidth()
                )

                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(
                        onClick = {
                            LocalDate.now()
                            DatePickerDialog(
                                ctx,
                                { _, y, m, d -> date = LocalDate.of(y, m + 1, d) },
                                date.year, date.monthValue - 1, date.dayOfMonth
                            ).show()
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(date.format(dateFmt))
                    }

                    OutlinedButton(
                        onClick = {
                            LocalTime.now()
                            TimePickerDialog(
                                ctx,
                                { _, h, min -> time = LocalTime.of(h, min) },
                                time.hour, time.minute, true
                            ).show()
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(time.format(timeFmt))
                    }
                }
            }
        }
    }
}
