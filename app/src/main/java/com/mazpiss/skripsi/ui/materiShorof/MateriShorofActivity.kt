package com.mazpiss.skripsi.ui.materiShorof

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.mazpiss.skripsi.ui.theme.ShorofTheme

class MateriShorofActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val subJudul = intent.getStringExtra("subJudul") ?: ""
        val content = intent.getStringExtra("content") ?: ""

        setContent {
            ShorofTheme {
                MateriShorofScreen(
                    subJudul = subJudul,
                    content = content,
                    onBackClick = { finish() }
                )
            }
        }
    }
}
