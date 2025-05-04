// app/src/main/java/com/example/cryptotracker/data/dao/AssetDao.kt
package com.example.cryptotracker.data.dao

import androidx.room.*
import com.example.cryptotracker.data.entity.CryptoAsset
import kotlinx.coroutines.flow.Flow

@Dao
interface AssetDao {

    /** Stream all saved assets **/
    @Query("SELECT * FROM assets")
    fun getAllAssetsFlow(): Flow<List<CryptoAsset>>

    /** One-time load of all assets **/
    @Query("SELECT * FROM assets")
    suspend fun getAllAssetsOnce(): List<CryptoAsset>

    /** Insert or replace an asset, returns its row ID **/
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(asset: CryptoAsset): Long

    /** Delete an asset, returns # of rows deleted **/
    @Delete
    suspend fun delete(asset: CryptoAsset): Int

    /** Update an existing asset, returns # of rows updated **/
    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(asset: CryptoAsset): Int
}