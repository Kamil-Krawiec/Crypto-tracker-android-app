// app/src/main/java/com/example/cryptotracker/data/network/TickerResponse.kt
package com.example.cryptotracker.data.network

import com.squareup.moshi.Json

data class TickerResponse(
    @Json(name = "symbol") val symbol: String,
    @Json(name = "price")  val price: String
) {
    /** parse the String price into a Double */
    val priceDouble: Double
        get() = price.toDoubleOrNull() ?: 0.0
}