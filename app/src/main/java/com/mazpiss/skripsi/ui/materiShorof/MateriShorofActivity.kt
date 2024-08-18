package com.mazpiss.skripsi.ui.materiShorof

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.mazpiss.skripsi.R
import com.mazpiss.skripsi.databinding.ActivityMateriShorofBinding

class MateriShorofActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMateriShorofBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMateriShorofBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val judul = intent.getStringExtra("judul")
        val subJudul = intent.getStringExtra("subJudul")
        val content = intent.getStringExtra("content")

        Log.d("MateriShorofActivity", "Received data: judul=$judul, subJudul=$subJudul, content=$content")

        if (!subJudul.isNullOrEmpty() && !content.isNullOrEmpty()) {
            binding.judulMateriShorof.text = subJudul
            binding.isiMateriShorof.text = content
        } else {
            binding.judulMateriShorof.text = "Data tidak tersedia"
            binding.isiMateriShorof.text = "Tidak ada konten yang ditemukan"
        }
    }
}