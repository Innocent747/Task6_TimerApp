package com.example.timerapp

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.timerapp.ui.theme.TimerAppTheme

class MainActivity : ComponentActivity() {

    private val requestNotificationPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { /* optional */ }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestNotificationPermission.launch(Manifest.permission.POST_NOTIFICATIONS)
        }

        setContent {
            TimerAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TimerScreen(
                        onStartTimer = { seconds ->
                            val intent = Intent(this, TimerService::class.java).apply {
                                putExtra(TimerService.EXTRA_SECONDS, seconds)
                            }
                            startService(intent)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun TimerScreen(onStartTimer: (Int) -> Unit) {
    var secondsText by remember { mutableStateOf("30") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = secondsText,
            onValueChange = { newValue ->
                if (newValue.all { it.isDigit() }) {
                    secondsText = newValue
                }
            },
            label = { Text("Секунды") },
            singleLine = true,
            modifier = Modifier.width(280.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                val sec = secondsText.toIntOrNull() ?: 0
                if (sec > 0) onStartTimer(sec)
            }
        ) {
            Text("Запустить таймер")
        }
    }
}