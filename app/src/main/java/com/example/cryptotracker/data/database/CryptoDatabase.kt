// app/src/main/java/com/example/cryptotracker/data/database/CryptoDatabase.kt
package com.example.cryptotracker.data.database

import android.content.Context
import androidx.room.*
import com.example.cryptotracker.data.Converters
import com.example.cryptotracker.data.dao.AssetDao
import com.example.cryptotracker.data.dao.AlertDao
import com.example.cryptotracker.data.entity.CryptoAsset
import com.example.cryptotracker.data.entity.PriceAlert

@Database(
    entities = [CryptoAsset::class, PriceAlert::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class CryptoDatabase : RoomDatabase() {
    abstract fun assetDao(): AssetDao
    abstract fun alertDao(): AlertDao

    companion object {
        @Volatile private var INSTANCE: CryptoDatabase? = null

        fun getInstance(context: Context): CryptoDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context): CryptoDatabase =
            Room.databaseBuilder(
                context.applicationContext,
                CryptoDatabase::class.java,
                "crypto_tracker_db"
            )
                // whenever version increases, Room will drop & recreate all tables
                .fallbackToDestructiveMigration()
                .build()
    }
}