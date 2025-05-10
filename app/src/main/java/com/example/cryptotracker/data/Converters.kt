// app/src/main/java/com/example/cryptotracker/data/Converters.kt
package com.example.cryptotracker.data

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.TypeConverter
import java.time.Instant

object Converters {
    @RequiresApi(Build.VERSION_CODES.O)
    @TypeConverter
    @JvmStatic
    fun fromInstant(value: Instant?): Long? =
        value?.toEpochMilli()

    @RequiresApi(Build.VERSION_CODES.O)
    @TypeConverter
    @JvmStatic
    fun toInstant(millis: Long?): Instant? =
        millis?.let { Instant.ofEpochMilli(it) }
}