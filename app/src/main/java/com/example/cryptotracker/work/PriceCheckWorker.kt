// app/src/main/java/com/example/cryptotracker/work/PriceCheckWorker.kt
package com.example.cryptotracker.work

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.cryptotracker.MainActivity
import com.example.cryptotracker.R
import com.example.cryptotracker.data.database.CryptoDatabase
import com.example.cryptotracker.data.entity.PriceAlert
import com.example.cryptotracker.data.network.NetworkModule
import com.example.cryptotracker.data.repository.AlertRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import androidx.core.net.toUri

class PriceCheckWorker(
    appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params) {

    companion object {
        const val WORK_NAME = "price_check_work"
        private const val CHANNEL_ID   = "price_alerts"
        private const val CHANNEL_NAME = "Price Alerts"
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("MissingPermission")
    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        val db  = CryptoDatabase.getInstance(applicationContext)
        val api = NetworkModule.getApi(applicationContext)
        val alertRepo = AlertRepository(db.alertDao(), api)

        return@withContext try {
            val triggered = alertRepo.checkAlerts()
            if (triggered.isNotEmpty()) {
                createNotificationChannel()
                triggered.forEach { alert ->
                    postAlertNotification(alert)
                }
            }
            Result.success()
        } catch (e: Throwable) {
            Result.retry()
        }
    }

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    private fun postAlertNotification(alert: PriceAlert) {
        val id         = alert.id.toInt()
        val symbol     = alert.symbol
        val threshold  = alert.targetPrice
        val direction  = if (alert.isAboveThreshold) "≥" else "≤"

        // 1. build an Intent with our deep-link URI
        val deepLinkUri = "cryptotracker://alert/${alert.id}".toUri()
        val intent = Intent(applicationContext, MainActivity::class.java).apply {
            action = Intent.ACTION_VIEW
            data   = deepLinkUri
        }
        val pendingIntent =PendingIntent.getActivity(
            applicationContext,
            id,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // 2. post the notification
        val notif = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("Price Alert: $symbol")
            .setContentText("$symbol is $direction $threshold")
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        NotificationManagerCompat.from(applicationContext)
            .notify(id, notif)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Notifies you when your price alerts are triggered"
        }
        (applicationContext.getSystemService(Context.NOTIFICATION_SERVICE)
                as NotificationManager)
            .createNotificationChannel(channel)
    }
}