package com.example.cryptotracker.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.cryptotracker.data.dao.AssetDao
import com.example.cryptotracker.data.dao.AlertDao
import com.example.cryptotracker.data.entity.CryptoAsset
import com.example.cryptotracker.data.entity.PriceAlert

@Database(
    entities = [CryptoAsset::class, PriceAlert::class],
    version = 1,
    exportSchema = false
)
abstract class CryptoDatabase : RoomDatabase() {
    abstract fun assetDao(): AssetDao
    abstract fun alertDao(): AlertDao

    companion object {
        @Volatile private var INSTANCE: CryptoDatabase? = null

        fun getInstance(context: Context): CryptoDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                CryptoDatabase::class.java,
                "crypto_tracker_db"
            ).build()
    }
}