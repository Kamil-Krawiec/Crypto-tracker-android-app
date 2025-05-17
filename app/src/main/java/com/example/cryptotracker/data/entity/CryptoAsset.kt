package com.example.cryptotracker.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant

@Entity(
    tableName = "assets",
)
data class CryptoAsset(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val symbol: String,            // e.g. "BTC"
    val name: String,              // full name
    val quantity: Double,          // e.g. 0.5
    val purchasePrice: Double,     // in USD
    val purchaseTimestamp: Instant,// when the asset was bought
    val imageUrl: String? = null   // optional icon URL
)