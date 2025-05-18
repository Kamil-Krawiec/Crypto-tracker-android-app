package com.example.cryptotracker.data.dao

import androidx.room.*
import com.example.cryptotracker.data.entity.PriceAlert
import kotlinx.coroutines.flow.Flow

@Dao
interface AlertDao {

    @Query("SELECT * FROM alerts ORDER BY createdAt DESC")
    fun getAllAlertsFlow(): Flow<List<PriceAlert>>

    @Query("SELECT * FROM alerts WHERE seen = 0 ORDER BY createdAt  DESC")
    suspend fun getAllAlertsOnce(): List<PriceAlert>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(alert: PriceAlert): Long

    @Delete
    suspend fun delete(alert: PriceAlert): Int

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(alert: PriceAlert): Int

    @Query("SELECT COUNT(*) FROM alerts WHERE seen = 0")
    fun getUnseenCountFlow(): Flow<Int>

    @Query("UPDATE alerts SET seen = 1 WHERE id = :id")
    suspend fun markSeen(id: Long)

    @Transaction
    suspend fun deleteAndInsert(replaceList: List<PriceAlert>) {
        deleteAll()
        replaceList.forEach { upsert(it) }
    }

    @Query("DELETE FROM alerts")
    suspend fun deleteAll(): Int
}
