package com.example.cryptotracker.ui.dashboard

data class DashboardItem(
    val symbol: String,
    val quantity: Double,
    val purchasePrice: Double,
    val livePrice: Double
) {
    val currentValue: Double get() = quantity * livePrice
    val investedValue: Double get()  = quantity * purchasePrice
    val profitLoss: Double get()     = currentValue - investedValue
}