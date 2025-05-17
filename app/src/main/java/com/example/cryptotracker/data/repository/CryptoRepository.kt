// app/src/main/java/com/example/cryptotracker/data/repository/CryptoRepository.kt
package com.example.cryptotracker.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.cryptotracker.data.dao.AssetDao
import com.example.cryptotracker.data.entity.CryptoAsset
import com.example.cryptotracker.data.network.CryptoApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import kotlinx.coroutines.flow.*
import java.time.Instant
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CryptoRepository @Inject constructor(
    private val dao: AssetDao,
    private val api: CryptoApi
){
    fun getAllAssets(): Flow<List<CryptoAsset>> =
        dao.getAllAssetsFlow()

    /** Bulk‐fetch all prices, wrapped in Resource **/
    fun getLivePrices(): Flow<Resource<Map<String, Double>>> = flow {
        emit(Resource.Loading)
        try {
            val list = api.getAllPrices()
            val map  = list.associate { resp ->
                resp.symbol to resp.price.toDoubleOrNull().orZero()
            }
            emit(Resource.Success(map))
        } catch (e: Throwable) {
            emit(Resource.Error(e))
        }
    }
        .flowOn(Dispatchers.IO)

    suspend fun upsertAsset(asset: CryptoAsset): Long =
        withContext(Dispatchers.IO) { dao.upsert(asset) }

    suspend fun deleteAsset(asset: CryptoAsset): Int =
        withContext(Dispatchers.IO) { dao.delete(asset) }

    suspend fun getLivePrice(symbol: String): Double =
        withContext(Dispatchers.IO) {
            api.getPrice("${symbol.uppercase()}USDT")
                .price
                .toDoubleOrNull()
                ?: 0.0
        }

    private fun Double?.orZero(): Double =
        this?.takeIf { !it.isNaN() } ?: 0.0

    /**
     * If user didn't type a purchase price but did pick a date/time,
     * pull exactly that 1m-bar from Binance:
     * GET /api/v3/klines?symbol=XXUSDT&interval=1m&startTime=<ms>&limit=1
     * and return the bar’s close price.
     * Falls back to live price on any error.
     */
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun getHistoricalPrice(
        symbol: String,
        at: Instant
    ): Double = withContext(Dispatchers.IO) {
        val pair = "${symbol.uppercase()}USDT"
        try {
            // request exactly one 1m bar at timestamp
            val millis = at.toEpochMilli()
            val bars = api.getCandlesticks(
                symbol   = pair,
                interval = "1m",
                limit    = 1,
                startTime = millis
            )
            if (bars.isNotEmpty() && bars[0].size > 4) {
                bars[0][4].toDoubleOrNull()
                    ?: bars[0][1].toDoubleOrNull() // fallback to open
                    ?: throw IllegalStateException("bad kline data")
            } else {
                throw IllegalStateException("no bar returned")
            }
        } catch (t: Throwable) {
            // …on any failure, just return current live price…
            getLivePrice(symbol)
        }
    }

    fun getPurchases(symbol: String): Flow<List<CryptoAsset>> =
        dao.getPurchasesFor(symbol)
}