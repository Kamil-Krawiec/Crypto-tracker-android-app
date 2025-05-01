package com.example.cryptotracker.data.dao

import androidx.room.*
import com.example.cryptotracker.data.entity.PriceAlert
import kotlinx.coroutines.flow.Flow

@Dao
interface AlertDao {

    /** Stream all saved alerts as a Flow **/
    @Query("SELECT * FROM alerts")
    fun observeAllAlerts(): Flow<List<PriceAlert>>

    /** Insert or replace an alert (no suspend) **/
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(alert: PriceAlert)

    /** Delete an alert (no suspend) **/
    @Delete
    fun delete(alert: PriceAlert)

    /** (Optional) Update an existing alert **/
    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(alert: PriceAlert): Int
}