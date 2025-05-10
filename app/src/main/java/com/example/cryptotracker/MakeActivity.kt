package com.example.cryptotracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import com.example.cryptotracker.data.database.CryptoDatabase
import com.example.cryptotracker.data.repository.AlertRepository
import com.example.cryptotracker.ui.makealert.MakeAlertScreen
import com.example.cryptotracker.ui.theme.CryptoTrackerTheme

class MakeActivity : ComponentActivity() {

    private val alertRepo by lazy {
        AlertRepository(
            CryptoDatabase.getInstance(this).alertDao()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CryptoTrackerTheme {
                Surface {
                    MakeAlertScreen(
                        alertRepo = alertRepo,
                        onDone = { finish() }
                    )
                }
            }
        }
    }
}