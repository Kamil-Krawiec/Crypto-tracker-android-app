// app/src/main/java/com/example/cryptotracker/MakeActivity.kt
package com.example.cryptotracker

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.material3.Surface
import com.example.cryptotracker.ui.makeAlert.MakeAlertScreen
import com.example.cryptotracker.ui.theme.CryptoTrackerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MakeActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CryptoTrackerTheme {
                Surface {
                    MakeAlertScreen(
                        onDone = { finish() }
                    )
                }
            }
        }
    }
}