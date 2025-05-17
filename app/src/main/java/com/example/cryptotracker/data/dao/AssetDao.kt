package com.example.cryptotracker.data.dao

import androidx.room.*
import com.example.cryptotracker.data.entity.CryptoAsset
import kotlinx.coroutines.flow.Flow

@Dao
interface AssetDao {

    @Query("SELECT * FROM assets ORDER BY purchaseTimestamp DESC")
    fun getAllAssetsFlow(): Flow<List<CryptoAsset>>

    @Query("SELECT * FROM assets ORDER BY purchaseTimestamp DESC")
    suspend fun getAllAssetsOnce(): List<CryptoAsset>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(asset: CryptoAsset): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(assets: List<CryptoAsset>)

    @Delete
    suspend fun delete(asset: CryptoAsset): Int

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(asset: CryptoAsset): Int

    @Transaction
    suspend fun replaceAll(assets: List<CryptoAsset>) {
        deleteAll()
        upsertAll(assets)
    }

    @Query("SELECT * FROM assets WHERE symbol = :symbol ORDER BY purchaseTimestamp DESC")
    fun getPurchasesFor(symbol: String): Flow<List<CryptoAsset>>

    @Query("DELETE FROM assets")
    suspend fun deleteAll(): Int
}
