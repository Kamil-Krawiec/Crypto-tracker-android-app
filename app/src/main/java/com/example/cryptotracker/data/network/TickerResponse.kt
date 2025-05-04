package com.example.cryptotracker.data.network

import com.squareup.moshi.Json

/**
 * Response from Binance’s /ticker/price endpoint.
 * Note: price comes back as a String in JSON.
 */
data class TickerResponse(
    @Json(name = "symbol") val symbol: String,
    @Json(name = "price")  val price: String
)