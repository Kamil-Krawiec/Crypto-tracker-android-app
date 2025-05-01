package com.example.cryptotracker.data.dao

import androidx.room.*
import com.example.cryptotracker.data.entity.CryptoAsset
import kotlinx.coroutines.flow.Flow

@Dao
interface AssetDao {

    /** Stream all saved assets as a Flow **/
    @Query("SELECT * FROM assets")
    fun observeAllAssets(): Flow<List<CryptoAsset>>

    /** Insert or replace an asset (no suspend) **/
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(asset: CryptoAsset)

    /** Delete an asset (no suspend) **/
    @Delete
    fun delete(asset: CryptoAsset)

    /** (Optional) Update an existing asset **/
    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(asset: CryptoAsset): Int
}