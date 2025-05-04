package com.example.cryptotracker

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import androidx.work.*
import com.example.cryptotracker.data.database.CryptoDatabase
import com.example.cryptotracker.data.repository.AlertRepository
import com.example.cryptotracker.data.repository.CryptoRepository
import com.example.cryptotracker.databinding.ActivityMainBinding
import com.example.cryptotracker.work.PriceCheckWorker
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    // Lazy‐init the singletons so we only ever build the DB once
    private val database by lazy {
        Room.databaseBuilder(
            applicationContext,
            CryptoDatabase::class.java,
            "crypto_tracker_db"
        ).build()
    }

    private val cryptoRepo by lazy { CryptoRepository(database.assetDao()) }
    private val alertRepo  by lazy { AlertRepository(database.alertDao()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbarAndNav()
        schedulePeriodicPriceChecks()
        observePendingAlerts()
        preloadSampleDataIfNeeded()
    }

    private fun setupToolbarAndNav() {
        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        // Set up dynamic toolbar titles
        val appBarConfig = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_dashboard,
                R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfig)
        navView.setupWithNavController(navController)
    }

    private fun schedulePeriodicPriceChecks() {
        // Every 15 minutes, fetch latest prices in the background
        val request = PeriodicWorkRequestBuilder<PriceCheckWorker>(15, TimeUnit.MINUTES)
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
                request
            )
    }

    private fun observePendingAlerts() {
        // Show a badge count on the notifications tab for any triggered-but-unseen alerts
        lifecycleScope.launchWhenStarted {
            alertRepo.getUnseenAlertCountFlow().collect { count ->
                val badge: BadgeDrawable =
                    binding.navView.getOrCreateBadge(R.id.navigation_notifications)
                badge.isVisible = count > 0
                badge.number    = count.coerceAtMost(99)
            }
        }
    }

    private fun preloadSampleDataIfNeeded() {
        // For first‐run demos: insert a couple of assets so the dashboard isn't empty
        lifecycleScope.launch(Dispatchers.IO) {
            if (cryptoRepo.getAllAssetsOnce().isEmpty()) {
                cryptoRepo.upsert(symbol = "BTC", quantity = 0.5, purchasePrice = 30000.0)
                cryptoRepo.upsertAsset(symbol = "ETH", quantity = 2.0, purchasePrice = 1500.0)
            }
        }
    }

    // Handle the up‐button in case you need it
    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.nav_host_fragment_activity_main).navigateUp()
    }
}