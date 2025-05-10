// app/src/main/java/com/example/cryptotracker/MakeActivity.kt
package com.example.cryptotracker

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.material3.Surface
import com.example.cryptotracker.data.database.CryptoDatabase
import com.example.cryptotracker.data.network.NetworkModule
import com.example.cryptotracker.data.repository.AlertRepository
import com.example.cryptotracker.ui.makealert.MakeAlertScreen
import com.example.cryptotracker.ui.theme.CryptoTrackerTheme

class MakeActivity : ComponentActivity() {

    // singletons for DB and API
    private val database by lazy { CryptoDatabase.getInstance(this) }
    private val api      by lazy { NetworkModule.getApi(this) }

    // now passes both DAO + API
    private val alertRepo by lazy {
        AlertRepository(
            dao = database.alertDao(),
            api = api
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CryptoTrackerTheme {
                Surface {
                    MakeAlertScreen(
                        alertRepo = alertRepo,
                        onDone    = { finish() }
                    )
                }
            }
        }
    }
}