package com.mazpiss.skripsi.ui.kosakata

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mazpiss.skripsi.databinding.ItemKosakataBinding

class KosakataAdapter(private val listKosakata: ArrayList<Kosakata>) : RecyclerView.Adapter<KosakataAdapter.ListViewHolder>() {

    inner class ListViewHolder(private val binding: ItemKosakataBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(kosakata: Kosakata) {
            binding.tvKosakata1.text = kosakata.kosakata1
            binding.tvKosakata2.text = kosakata.kosakata2
            binding.tvTerjemahan1.text = kosakata.terjemahan1
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemKosakataBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun getItemCount(): Int = listKosakata.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listKosakata[position])
    }
}
