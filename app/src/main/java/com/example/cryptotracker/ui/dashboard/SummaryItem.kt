// app/src/main/java/com/example/cryptotracker/ui/dashboard/SummaryItem.kt
package com.example.cryptotracker.ui.dashboard

data class SummaryItem(
    val symbol: String,
    val totalQuantity: Double,
    val averagePrice: Double,
    val livePrice: Double
) {
    val currentValue: Double
        get() = totalQuantity * livePrice

    val investedValue: Double
        get() = totalQuantity * averagePrice

    val profitLoss: Double
        get() = currentValue - investedValue

    val percentChange: Double
        get() = if (investedValue > 0.0) profitLoss / investedValue * 100.0 else 0.0
}