// AppModule.kt
package com.example.cryptotracker.di

import android.content.Context
import androidx.room.Room
import com.example.cryptotracker.data.database.CryptoDatabase
import com.example.cryptotracker.data.dao.AssetDao
import com.example.cryptotracker.data.dao.AlertDao
import com.example.cryptotracker.data.network.NetworkModule
import com.example.cryptotracker.data.network.CryptoApi
import com.example.cryptotracker.data.repository.CryptoRepository
import com.example.cryptotracker.data.repository.AlertRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides @Singleton
    fun provideDatabase(@ApplicationContext ctx: Context): CryptoDatabase =
        Room.databaseBuilder(ctx, CryptoDatabase::class.java, "crypto_db")
            .fallbackToDestructiveMigration()
            .build()

    @Provides fun provideAssetDao(db: CryptoDatabase): AssetDao = db.assetDao()
    @Provides fun provideAlertDao(db: CryptoDatabase): AlertDao = db.alertDao()

    @Provides @Singleton
    fun provideCryptoApi(@ApplicationContext ctx: Context): CryptoApi =
        NetworkModule.getApi(ctx)

    @Provides @Singleton
    fun provideCryptoRepository(
        dao: AssetDao,
        api: CryptoApi
    ): CryptoRepository = CryptoRepository(dao, api)

    @Provides @Singleton
    fun provideAlertRepository(
        dao: AlertDao,
        api: CryptoApi
    ): AlertRepository = AlertRepository(dao, api)
}