package com.example.cryptotracker.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "assets")
data class CryptoAsset(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val symbol: String,       // e.g. "BTC"
    val quantity: Double,     // e.g. 0.5
    val purchasePrice: Double // in USD
)