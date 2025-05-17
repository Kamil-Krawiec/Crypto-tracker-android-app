// app/src/main/java/com/example/cryptotracker/ui/dashboard/PurchaseItem.kt
package com.example.cryptotracker.ui.assetDetail

import java.time.Instant

data class PurchaseItem(
    val id: Long,
    val symbol: String,
    val name: String,
    val quantity: Double,
    val purchasePrice: Double,
    val purchaseTimestamp: Instant,
    val livePrice: Double,
    val imageUrl: String?
) {
    val investedValue: Double get() = quantity * purchasePrice
    val currentValue:  Double get() = quantity * livePrice
    val profitLoss:    Double get() = currentValue - investedValue
    val percentChange: Double get() = if (investedValue > 0) profitLoss / investedValue * 100 else 0.0
}