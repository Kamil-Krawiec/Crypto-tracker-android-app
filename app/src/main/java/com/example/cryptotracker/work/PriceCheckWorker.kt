package com.example.cryptotracker.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.cryptotracker.data.database.CryptoDatabase
import com.example.cryptotracker.data.repository.CryptoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PriceCheckWorker(
    appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            val dao  = CryptoDatabase.getInstance(applicationContext).assetDao()
            val repo = CryptoRepository(dao)
            // TODO: fetch live prices and update your local DB or fire alerts
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}