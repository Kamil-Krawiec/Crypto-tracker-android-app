package com.example.cryptotracker.data.network

import com.squareup.moshi.Json

data class OrderBookResponse(
    @Json(name = "lastUpdateId") val lastUpdateId: Long,
    @Json(name = "bids") val bids: List<List<String>>,
    @Json(name = "asks") val asks: List<List<String>>
)