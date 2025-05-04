package com.example.cryptotracker.data.repository

import com.example.cryptotracker.data.dao.AssetDao
import com.example.cryptotracker.data.entity.CryptoAsset
import com.example.cryptotracker.data.network.NetworkModule
import kotlinx.coroutines.flow.Flow

class CryptoRepository(private val dao: AssetDao) {

    /** Streams all assets as a Flow **/
    fun getAllAssets(): Flow<List<CryptoAsset>> =
        dao.getAllAssetsFlow()

    /** One-time load of all assets **/
    suspend fun getAllAssetsOnce(): List<CryptoAsset> =
        dao.getAllAssetsOnce()

    /** Insert or update an asset, returns the row ID **/
    suspend fun upsertAsset(asset: CryptoAsset): Long =
        dao.upsert(asset)

    /** Delete an asset, returns number of rows deleted **/
    suspend fun deleteAsset(asset: CryptoAsset): Int =
        dao.delete(asset)

    /** Update an existing asset, returns number of rows updated **/
    suspend fun updateAsset(asset: CryptoAsset): Int =
        dao.update(asset)

    /**
     * Fetches the live price for a symbol by calling the network API.
     * E.g. symbol = "BTC" → fetch "BTCUSDT"
     */
    suspend fun getLivePrice(symbol: String): Double {
        val response = NetworkModule
            .api
            .getPrice("${symbol.uppercase()}USDT")
        return response.price.toDouble()
    }
}