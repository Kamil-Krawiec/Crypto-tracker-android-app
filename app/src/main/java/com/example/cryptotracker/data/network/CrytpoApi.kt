package com.example.cryptotracker.data.network

import retrofit2.http.GET
import retrofit2.http.Query

interface CryptoApi {
    /** Single‐symbol price **/
    @GET("api/v3/ticker/price")
    suspend fun getPrice(
        @Query("symbol") symbol: String
    ): TickerResponse

    /** All tickers at once **/
    @GET("api/v3/ticker/price")
    suspend fun getAllPrices(): List<TickerResponse>

    /**
     * Fetch OHLC bars.  If you pass startTime (ms since epoch) and limit=1,
     * you’ll get exactly the bar that includes that timestamp.
     */
    @GET("api/v3/klines")
    suspend fun getCandlesticks(
        @Query("symbol") symbol: String,
        @Query("interval") interval: String,
        @Query("limit") limit: Int = 100,
        @Query("startTime") startTime: Long? = null,
        @Query("endTime")   endTime:   Long? = null
    ): List<List<String>>



}