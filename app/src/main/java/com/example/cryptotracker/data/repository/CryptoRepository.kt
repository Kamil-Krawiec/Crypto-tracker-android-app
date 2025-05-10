// app/src/main/java/com/example/cryptotracker/data/repository/CryptoRepository.kt
package com.example.cryptotracker.data.repository

import com.example.cryptotracker.data.dao.AssetDao
import com.example.cryptotracker.data.entity.CryptoAsset
import com.example.cryptotracker.data.network.CryptoApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import kotlinx.coroutines.flow.*

class CryptoRepository(
    private val dao: AssetDao,
    private val api: CryptoApi
) {
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
}