package com.example.cryptotracker

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.work.*
import com.example.cryptotracker.ui.NavGraph
import com.example.cryptotracker.ui.theme.CryptoTrackerTheme
import com.example.cryptotracker.work.PriceCheckWorker
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    // Launcher for requesting a single permission
    private val notifPermissionLauncher = registerForActivityResult(RequestPermission()) { granted ->
        if (granted) {
            schedulePriceChecks()
        }
        // if not granted, you might show a dialog or disable alerts UI
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // If on Android 13+ ask for POST_NOTIFICATIONS, otherwise start right away
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            when {
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == android.content.pm.PackageManager.PERMISSION_GRANTED -> {
                    // already approved
                    schedulePriceChecks()
                }
                else -> {
                    // request it
                    notifPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        } else {
            schedulePriceChecks()
        }

        setContent {
            CryptoTrackerTheme {
                NavGraph()
            }
        }
    }

    private fun schedulePriceChecks() {
        // 1) Periodic work every 15 minutes
        val periodicRequest = PeriodicWorkRequestBuilder<PriceCheckWorker>(
            15, TimeUnit.MINUTES
        )
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
            )
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            PriceCheckWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            periodicRequest
        )

        // 2) One-off, immediate work to fire your first check ASAP
        val immediateRequest = OneTimeWorkRequestBuilder<PriceCheckWorker>()
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
            )
            .build()

        WorkManager.getInstance(this).enqueue(immediateRequest)
    }
}