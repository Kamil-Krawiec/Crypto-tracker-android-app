package com.example.cryptotracker
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.cryptotracker.work.PriceCheckWorker
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.cryptotracker.data.database.CryptoDatabase
import com.example.cryptotracker.data.repository.AlertRepository
import com.example.cryptotracker.data.repository.CryptoRepository
import com.example.cryptotracker.ui.theme.CryptoTrackerTheme
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {

    private val database by lazy {
        CryptoDatabase.getInstance(applicationContext)
    }
    private val cryptoRepo by lazy {
        CryptoRepository(database.assetDao())
    }
    private val alertRepo by lazy {
        AlertRepository(database.alertDao())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        schedulePriceChecks()

        // set up Compose content
        setContent {
            CryptoTrackerTheme {
                CryptoTrackerNavGraph(
                    cryptoRepo = CryptoRepository(CryptoDatabase.getInstance(this).assetDao()),
                    alertRepo  = AlertRepository(  CryptoDatabase.getInstance(this).alertDao())
                )
            }
        }
    }

    private fun schedulePriceChecks() {
        val work = PeriodicWorkRequestBuilder<PriceCheckWorker>(
            15, TimeUnit.MINUTES
        )
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
            )
            .build()
        WorkManager.getInstance(this)
            .enqueueUniquePeriodicWork(
                "price_check",
                ExistingPeriodicWorkPolicy.KEEP,
                work
            )
    }
}