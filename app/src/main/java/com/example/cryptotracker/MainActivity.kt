// app/src/main/java/com/example/cryptotracker/MainActivity.kt
package com.example.cryptotracker

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.work.*
import com.example.cryptotracker.ui.NavGraph
import com.example.cryptotracker.ui.theme.CryptoTrackerTheme
import com.example.cryptotracker.work.PriceCheckWorker
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        schedulePriceChecks()
        setContent {
            CryptoTrackerTheme {
                NavGraph()
            }
        }
    }

    private fun schedulePriceChecks() {
        val work = PeriodicWorkRequestBuilder<PriceCheckWorker>(4, TimeUnit.SECONDS)
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
            ).build()
        WorkManager.getInstance(this)
            .enqueueUniquePeriodicWork(
                PriceCheckWorker.WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                work
            )
    }
}