package com.mazpiss.skripsi.ui.materiDetail

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.mazpiss.skripsi.ui.materiShorof.MateriShorofActivity
import com.mazpiss.skripsi.ui.theme.ShorofTheme

class DetailMateriActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val materiJudul = intent.getStringExtra("urutan")

        setContent {
            ShorofTheme {
                DetailMateriScreen(
                    materiJudul = materiJudul,
                    onBackClick = { finish() },
                    onSubMateriClick = { subMateri ->
                        val intent = Intent(this, MateriShorofActivity::class.java).apply {
                            putExtra("subJudul", subMateri.subJudul)
                            putExtra("content", subMateri.content)
                        }
                        startActivity(intent)
                    }
                )
            }
        }
    }
}
