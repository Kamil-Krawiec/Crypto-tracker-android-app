package com.example.cryptotracker.data.network

import retrofit2.http.GET
import retrofit2.http.Query

interface CryptoApi {
    /**
     * Fetch the current price for a single symbol (e.g. BTCUSDT).
     */
    @GET("api/v3/ticker/price")
    suspend fun getPrice(
        @Query("symbol") symbol: String
    ): TickerResponse

    @GET("api/v3/ticker/price")
    suspend fun getAllPrices(): List<TickerResponse>

}