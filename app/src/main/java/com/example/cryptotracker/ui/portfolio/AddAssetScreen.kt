// app/src/main/java/com/example/cryptotracker/ui/portfolio/AddAssetScreen.kt
package com.example.cryptotracker.ui.portfolio

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import java.time.*
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddAssetScreen(
    onSaved: () -> Unit,
    viewModel: AddAssetViewModel = hiltViewModel()
) {
    val ctx = LocalContext.current

    var symbol    by remember { mutableStateOf("") }
    var qtyText   by remember { mutableStateOf("")  }
    var priceText by remember { mutableStateOf("") }
    var date      by remember { mutableStateOf<LocalDate?>(null) }
    var time      by remember { mutableStateOf<LocalTime?>(null) }

    // must have symbol+qty, and either price or date+time
    val isValid = remember(symbol, qtyText, priceText, date, time) {
        symbol.isNotBlank()
                && qtyText.toDoubleOrNull()?.let { it > 0 } == true
                && (
                priceText.toDoubleOrNull()?.let { it > 0 } != null
                        || (date != null && time != null)
                )
    }

    val dateFmt = DateTimeFormatter.ofPattern("MMM dd, yyyy")
    val timeFmt = DateTimeFormatter.ofPattern("HH:mm")

    Scaffold(
        topBar = { TopAppBar(title = { Text("Add New Asset") }) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (!isValid) return@FloatingActionButton
                    val qty   = qtyText.toDouble()
                    val expPr = priceText.toDoubleOrNull()
                    val ts    = expPr?.let { Instant.now() }
                        ?: date!!.atTime(time!!)
                            .atZone(ZoneId.systemDefault())
                            .toInstant()

                    viewModel.saveAsset(
                        symbol            = symbol.trim().uppercase(),
                        name              = "",               // …or collect a name field
                        quantity          = qty,
                        explicitPrice     = expPr,
                        purchaseTimestamp = ts,
                        imageUrl          = null
                    )
                    onSaved()
                },
                modifier = Modifier.alpha(if (isValid) 1f else 0.5f)
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
            // **editable** text field — keyboard WILL pop up
            OutlinedTextField(
                value           = symbol,
                modifier = Modifier.fillMaxWidth(),
                onValueChange   = { symbol = it },
                label           = { Text("Symbol (e.g. BTC)") },
                singleLine      = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                isError         = symbol.isBlank()
            )

            OutlinedTextField(
                value           = qtyText,
                onValueChange   = { qtyText = it },
                modifier = Modifier.fillMaxWidth(),
                label           = { Text("Quantity") },
                singleLine      = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError         = qtyText.toDoubleOrNull() == null
            )

            OutlinedTextField(
                value           = priceText,
                onValueChange   = { priceText = it },
                label           = { Text("Price (blank = historical)") },
                singleLine      = true,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError         = priceText.isNotBlank() && priceText.toDoubleOrNull() == null
            )

            // two buttons to pick date & time
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(
                    onClick = {
                        val today = LocalDate.now()
                        DatePickerDialog(
                            ctx,
                            { _, y, m, d ->
                                date = LocalDate.of(y, m + 1, d)
                            },
                            today.year, today.monthValue - 1, today.dayOfMonth
                        ).show()
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.CalendarToday, contentDescription = "Pick date")
                    Spacer(Modifier.width(4.dp))
                    Text(date?.format(dateFmt) ?: "Select date")
                }

                OutlinedButton(
                    onClick = {
                        val now = LocalTime.now()
                        TimePickerDialog(
                            ctx,
                            { _, h, min ->
                                time = LocalTime.of(h, min)
                            },
                            now.hour, now.minute, true
                        ).show()
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Schedule, contentDescription = "Pick time")
                    Spacer(Modifier.width(4.dp))
                    Text(time?.format(timeFmt) ?: "Select time")
                }
            }
        }
    }
}