package com.mazpiss.skripsi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.mazpiss.skripsi.ui.navigation.ShorofApp
import com.mazpiss.skripsi.ui.theme.ShorofTheme

class HomeActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ShorofTheme {
                ShorofApp()
            }
        }
    }
}
