package com.example.cryptotracker.data.entity

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Index
import java.time.Instant

@Entity(
    tableName = "alerts",
    indices = [Index(value = ["symbol", "targetPrice"], unique = true)]
)
data class PriceAlert @RequiresApi(Build.VERSION_CODES.O) constructor(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val symbol: String,             // e.g. "ETH"
    val targetPrice: Double,        // threshold price
    val isAboveThreshold: Boolean,  // true = notify ≥ target, false = ≤
    val seen: Boolean = false,      // has user viewed it?
    val createdAt: Instant = Instant.now(),    // when alert was created
    val triggeredAt: Instant? = null           // when triggered
)