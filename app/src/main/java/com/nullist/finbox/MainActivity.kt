package com.nullist.finbox

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import com.nullist.finbox.ui.theme.AlarmDownloadManagerTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AlarmDownloadManagerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold(topBar = {
                        TopAppBar(
                            title = {
                                Text("Finbox")
                            },
                            actions = {
                                Switch(
                                    checked = false,
                                    onCheckedChange = {
                                    }
                                )
                                IconButton(onClick = {}) {
                                    Icon(Icons.Filled.Add, "")
                                }
                                IconButton(onClick = { /*TODO*/ }) {
                                    Icon(Icons.Filled.Settings, "")
                                }
                            }
                        )
                    }) {
                        Text(
                            "Test",
                            modifier = Modifier.padding(it)
                        )
                    }
                }
            }
        }
    }
}
