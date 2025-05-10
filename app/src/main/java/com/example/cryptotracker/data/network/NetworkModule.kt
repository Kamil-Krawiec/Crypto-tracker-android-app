// app/src/main/java/com/example/cryptotracker/data/network/NetworkModule.kt
package com.example.cryptotracker.data.network

import android.content.Context
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

object NetworkModule {
    private const val BASE_URL    = "https://api.binance.com/"
    private const val CACHE_SIZE  = 5L * 1024 * 1024   // 5 MiB
    private const val TIMEOUT_SEC = 30L

    @Volatile
    private var apiInstance: CryptoApi? = null

    /**
     * Call this to get your singleton CryptoApi.
     * Pass an Android Context so we can set up the HTTP cache directory.
     */
    fun getApi(context: Context): CryptoApi =
        apiInstance ?: synchronized(this) {
            apiInstance ?: buildApi(context).also { apiInstance = it }
        }

    private fun buildApi(context: Context): CryptoApi {
        val cache = Cache(File(context.cacheDir, "http_cache"), CACHE_SIZE)

        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.NONE
        }

        val retryInterceptor = Interceptor { chain ->
            var attempt = 0
            var response = chain.proceed(chain.request())
            while (!response.isSuccessful && attempt < 3) {
                attempt++
                response.close()
                response = chain.proceed(chain.request())
            }
            response
        }

        val client = OkHttpClient.Builder()
            .cache(cache)
            .addInterceptor(retryInterceptor)
            .addInterceptor(logging)
            .connectTimeout(TIMEOUT_SEC, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT_SEC, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .build()

        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

        return retrofit.create(CryptoApi::class.java)
    }
}