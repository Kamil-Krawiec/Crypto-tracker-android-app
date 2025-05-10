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

    /** Historical candlesticks (OHLC) **/
    @GET("api/v3/klines")
    suspend fun getCandlesticks(
        @Query("symbol") symbol: String,
        @Query("interval") interval: String,
        @Query("limit") limit: Int = 100
    ): List<List<String>>

    /** Order book (bids & asks) **/
    @GET("api/v3/depth")
    suspend fun getOrderBook(
        @Query("symbol") symbol: String,
        @Query("limit") limit: Int = 100
    ): OrderBookResponse
}