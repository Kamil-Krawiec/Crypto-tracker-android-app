package com.example.cryptotracker.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "alerts")
data class PriceAlert(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val symbol: String,            // e.g. "ETH"
    val targetPrice: Double,       // threshold price
    val isAboveThreshold: Boolean, // true = notify when ≥ target, false = ≤ target
    val seen: Boolean = false      // has the user already viewed this alert?
)