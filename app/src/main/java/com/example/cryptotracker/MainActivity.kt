// app/src/main/java/com/example/cryptotracker/MainActivity.kt
package com.example.cryptotracker

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.cryptotracker.data.database.CryptoDatabase
import com.example.cryptotracker.data.network.NetworkModule
import com.example.cryptotracker.data.repository.AlertRepository
import com.example.cryptotracker.data.repository.CryptoRepository
import com.example.cryptotracker.ui.CryptoTrackerNavGraph
import com.example.cryptotracker.ui.theme.CryptoTrackerTheme
import com.example.cryptotracker.work.PriceCheckWorker
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {

    // single database instance
    private val database by lazy {
        CryptoDatabase.getInstance(applicationContext)
    }

    // single Retrofit API instance
    private val api by lazy {
        NetworkModule.getApi(applicationContext)
    }

    // repositories now take both DAO and API
    private val cryptoRepo by lazy {
        CryptoRepository(database.assetDao(), api)
    }
    private val alertRepo by lazy {
        AlertRepository(database.alertDao(), api)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        schedulePriceChecks()

        setContent {
            CryptoTrackerTheme {
                CryptoTrackerNavGraph(
                    cryptoRepo = cryptoRepo,
                    alertRepo = alertRepo
                )
            }
        }
    }

    private fun schedulePriceChecks() {
        val work = PeriodicWorkRequestBuilder<PriceCheckWorker>(
            5, TimeUnit.SECONDS
        )
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
            )
            .build()

        WorkManager.getInstance(this)
            .enqueueUniquePeriodicWork(
                toString(),
                ExistingPeriodicWorkPolicy.KEEP,
                work
            )
    }
}