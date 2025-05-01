package com.example.cryptotracker.data.database

import androidx.room.Database
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
}