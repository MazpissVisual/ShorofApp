package com.mazpiss.skripsi.ui.faq

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mazpiss.skripsi.R

class FaqActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private val mList = mutableListOf<FaqData>()
    private lateinit var adapter: FaqAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_faq)

        setupViews()
        setupRecyclerView()
        addDataToList()
    }

    private fun setupViews() {
        recyclerView = findViewById(R.id.rvFaq)
    }

    private fun setupRecyclerView() {
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = FaqAdapter(mList)
        recyclerView.adapter = adapter
    }

    private fun addDataToList() {
        mList.apply {
            add(FaqData("Persiapan Sebelum Latihan", "Sebelum memulai latihan, pastikan kamu telah membaca dan memahami materi yang dipelajari. Jangan lupa untuk berdoa agar diberikan kemudahan dan kelancaran dalam mengerjakannya. Semangat belajar!"))
            add(FaqData("Instruktur Umum", "1. Setiap latihan memiliki durasi pengerjaan yang berbeda-beda, jadi pastikan kamu memeriksa waktu yang tersedia sebelum memulai.\n\n2. Jenis Soal dalam latihan ini berbentuk pilihan ganda, sehingga kamu bisa memilih jawaban yang paling tepat dari beberapa opsi yang diberikan."))
            add(FaqData("Langkah Pengerjaan", "1. Silakan pilih latihan yang ingin kamu kerjakan.\n\n" +
                    "2. Pilih salah satu jawaban yang paling benar dari setiap soal yang diberikan.\n\n" +
                    "3. Nilai akhir akan muncul setelah kamu menyelesaikan semua soal."))
            add(FaqData("Tips dan Trik", "Sebelum mulai latihan, pastikan kamu memahami semua materi. Jika ada yang masih belum jelas, jangan ragu untuk berkonsultasi dengan pengajar."))
            }
        adapter.notifyDataSetChanged()
    }
}