package com.example.cryptotracker.data.repository

import com.example.cryptotracker.data.dao.AlertDao
import com.example.cryptotracker.data.entity.PriceAlert
import com.example.cryptotracker.data.network.NetworkModule
import kotlinx.coroutines.flow.Flow

class AlertRepository(private val dao: AlertDao) {

    /** Streams all alerts as a Flow **/
    fun getAllAlerts(): Flow<List<PriceAlert>> =
        dao.getAllAlertsFlow()

    /** One-time load of all alerts **/
    suspend fun getAllAlertsOnce(): List<PriceAlert> =
        dao.getAllAlertsOnce()

    /** Count of unseen alerts **/
    fun getUnseenAlertCount(): Flow<Int> =
        dao.getUnseenCountFlow()

    /** Insert or update an alert, returns the row ID **/
    suspend fun upsertAlert(alert: PriceAlert): Long =
        dao.upsert(alert)

    /** Delete an alert, returns number of rows deleted **/
    suspend fun deleteAlert(alert: PriceAlert): Int =
        dao.delete(alert)

    /** Mark an alert as seen **/
    suspend fun markAlertSeen(id: Long): Unit =
        dao.markSeen(id)

    /**
     * Called by your Worker to fetch all alerts once
     * and return only those whose threshold has been triggered.
     */
    suspend fun checkAlerts(): List<PriceAlert> {
        val fired = mutableListOf<PriceAlert>()
        // load once rather than collecting the flow
        dao.getAllAlertsOnce().forEach { alert ->
            val currentPrice = NetworkModule
                .api
                .getPrice("${alert.symbol.uppercase()}USDT")
                .price
                .toDouble()
            if ((alert.isAboveThreshold && currentPrice >= alert.targetPrice) ||
                (!alert.isAboveThreshold && currentPrice <= alert.targetPrice)
            ) {
                fired += alert
            }
        }
        return fired
    }
}