// app/src/main/java/com/example/cryptotracker/data/dao/AlertDao.kt
package com.example.cryptotracker.data.dao

import androidx.room.*
import com.example.cryptotracker.data.entity.PriceAlert
import kotlinx.coroutines.flow.Flow

@Dao
interface AlertDao {

    /** Stream all saved alerts **/
    @Query("SELECT * FROM alerts")
    fun getAllAlertsFlow(): Flow<List<PriceAlert>>

    /** One-time load of all alerts **/
    @Query("SELECT * FROM alerts")
    suspend fun getAllAlertsOnce(): List<PriceAlert>

    /** Insert or replace an alert, returns its new row ID **/
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(alert: PriceAlert): Long

    /** Delete an alert, returns # of rows deleted **/
    @Delete
    suspend fun delete(alert: PriceAlert): Int

    /** Update an existing alert, returns # of rows updated **/
    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(alert: PriceAlert): Int

    /** Count of alerts not yet seen **/
    @Query("SELECT COUNT(*) FROM alerts WHERE seen = 0")
    fun getUnseenCountFlow(): Flow<Int>

    /** Mark a single alert as seen **/
    @Query("UPDATE alerts SET seen = 1 WHERE id = :id")
    suspend fun markSeen(id: Long)
}