package com.mazpiss.skripsi.ui.materiDetail

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.mazpiss.skripsi.databinding.ActivityDetailMateriBinding
import com.mazpiss.skripsi.ui.detail.DetailViewModel


class DetailMateriActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailMateriBinding
    private lateinit var viewModel: DetailViewModel
    private lateinit var myAdapter: SubMateriAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailMateriBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val materiId  = intent.getStringExtra("urutan")

        viewModel = ViewModelProvider(this)[DetailViewModel::class.java]
        viewModel.loadSubMateri(materiId )

        binding.rvDetailMateri.layoutManager = LinearLayoutManager(this)
        binding.rvDetailMateri.setHasFixedSize(true)
        myAdapter = SubMateriAdapter(arrayListOf())
        binding.rvDetailMateri.adapter = myAdapter

        viewModel.subMateriList.observe(this) { subMateriList ->
            myAdapter.updateData(subMateriList)
        }
    }
}
