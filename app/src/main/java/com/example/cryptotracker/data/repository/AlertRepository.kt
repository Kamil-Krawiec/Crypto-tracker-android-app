// app/src/main/java/com/example/cryptotracker/data/repository/AlertRepository.kt
package com.example.cryptotracker.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.cryptotracker.data.dao.AlertDao
import com.example.cryptotracker.data.entity.PriceAlert
import com.example.cryptotracker.data.network.CryptoApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.time.Instant
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AlertRepository @Inject constructor(
    private val dao: AlertDao,
    private val api: CryptoApi
) {

    /** Streams all alerts as a Flow **/
    fun getAllAlerts(): Flow<List<PriceAlert>> =
        dao.getAllAlertsFlow()

    /** Insert or update an alert, returns the row ID **/
    suspend fun upsertAlert(alert: PriceAlert): Long =
        withContext(Dispatchers.IO) {
            dao.upsert(alert)
        }

    /** Delete an alert, returns number of rows deleted **/
    suspend fun deleteAlert(alert: PriceAlert): Int =
        withContext(Dispatchers.IO) {
            dao.delete(alert)
        }

    /** Mark an alert as seen **/
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun markAlertSeen(id: Long): Unit =
        withContext(Dispatchers.IO) {
            dao.markSeen(id)
        }

    /**
     * Check all alerts against live prices in one bulk call.
     * Marks each triggered alert as seen and stamps triggeredAt.
     * Returns the list of fired alerts.
     */
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun checkAlerts(): List<PriceAlert> = withContext(Dispatchers.IO) {
        // 1. Fetch all prices once
        val pricesMap = api
            .getAllPrices()
            .associate { it.symbol to it.price.toDoubleOrNull().orZero() }

        // 2. Find which alerts have fired
        val now = Instant.now()
        val fired = dao.getAllAlertsOnce().filter { alert ->
            pricesMap["${alert.symbol.uppercase()}USDT"]?.let { price ->
                if (alert.isAboveThreshold) price >= alert.targetPrice
                else price <= alert.targetPrice
            } == true
        }

        // 3. Update each fired alert: mark seen & set triggeredAt

        fired.forEach { alert ->
            if (alert.triggeredAt == null) {
                val updated = alert.copy(triggeredAt = now)
                dao.update(updated)
            }
        }
        fired
    }

    fun getAlertById(id: Long): Flow<PriceAlert?> =
        dao.getAllAlertsFlow()
            .map { list -> list.find { it.id == id } }

    /** Utility extension: treat null or NaN as zero */
    private fun Double?.orZero(): Double = this?.takeIf { !it.isNaN() } ?: 0.0
}